package com.sg.pcrf.cmpp

import com.sg.pcrf.exceptions.CMPPException
import com.sg.pcrf.util.sftpDownLog.PCCDownLog
import com.sg.pcrf.util.sftpDownLog.ValueComparator
import grails.transaction.Transactional
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.support.PropertiesLoaderUtils

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import soap.BasePmSoap

@Transactional
class UserNotificationRecordService {
    def grailsApplication

    def serviceMethod() {

    }
    def getConfigProperties() {
        def path = grailsApplication.config.basepath
        def resource = new FileSystemResource("${path}${File.separator}config${File.separator}cmpp.properties")
        def properties = PropertiesLoaderUtils.loadProperties(resource)
        return properties
    }

    /**
     * 复制整个文件夹内容
     *
     */
    def copyFolder(String oldPath, String newPath, String[] files) {
        log.info("Copy files to backup")
        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = files;
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i], files);
                }
            }
            log.info("End copy files")
            this.delAllFile(oldPath, files);
        }
        catch (Exception e) {
            System.out.println("copy file error");
            e.printStackTrace();

        }
    }

    //删除指定文件夹下所有文件
//param path 文件夹完整绝对路径
    def delAllFile(String path, String[] files) {
        log.info("Delete download file")
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        /*String[] tempList = file.list();*/
        String[] tempList = files;
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i], files);//先删除文件夹里面的文件
                //delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        log.info("End delete download file")
        return flag;
    }

    //获取本地log文件
    @Transactional
    def parseLog() {
        log.info("CMPP data acquisition begins")
        Integer minute=1;
        def task = new TimerTask() {
            @Override
            public void run() {
                //获取配置文件信息
                def map=getConfigProperties()
               // def map = getJsonContext("1")
                String configurePath = map.get("configurePath")
                minute = Integer.parseInt(map.get("minute"))
                String downPath = map.get("downPath")
                File fileDir = new File(downPath)
                if (!fileDir.exists()) {
                    fileDir.mkdirs()
                }
                log.info("Begin connecting to the SFTP server and downloading the log file")
                String path=null;//记录下载后的日志文件路径
                try {
                    def pccd = new PCCDownLog()
                    path = pccd.downPCCLog(downPath,map.get("host"),map.get("username"),map.get("password"),map.get("port"))
                    log.info("The end of the download log file")
                } catch (CMPPException e) {
                    log.error(e.printStackTrace())
                    log.info("Failed to connect to the SFTP server or download the log file")
                }
                // Get address path
                File file = new File(path)
                //Get all log file name
                String[] names = file.list()
                Map<String, Long> getTime = new HashMap<String, Long>()
                ValueComparator bvc = new ValueComparator(getTime)
                TreeMap<String, Long> sorted_map = new TreeMap<String, Long>(bvc)
                for (int f = 0; f < names.length; f++) {
                    File files = new File(path + "/" + names[f])
                    long time = files.lastModified()
                    getTime.put(names[f], time)
                }
                sorted_map.putAll(getTime)
                Set set = sorted_map.keySet()
                def folderName = (String[]) set.toArray()
                //Declare object
                UserNotificationRecord cl = new UserNotificationRecord()
                String temporaryFile = path.substring(0, path.lastIndexOf("/"))//linux
                //String temporaryFile = path.substring(0, path.lastIndexOf("\\"))//windows
                File Logfile = new File(temporaryFile + "/logRecord")// 存储记录节点的文件夹
                if (!Logfile.exists()) {//是否存在目录文件
                    Logfile.mkdirs()// 创建文件夹
                }
                File LogIndex = new File(temporaryFile + "/logRecord/logRecord.txt")// 记录节点的文件
                if (!LogIndex.exists()) {// 判断文件是否存在
                    try {
                        LogIndex.createNewFile()// 创建txt文件
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                }
                // Get file content
                java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.FileReader(LogIndex))
                String s = reader.readLine();// 读取日志文件的一行记录
                int index = 0;// 记录读取的第几条日志文件
                String logFileName = null
                //Integer number=0
                if (s != null) {// 判断是否记录了节点
                    logFileName = s.substring(0, s.indexOf(":"))
                    //number = Integer.parseInt(s.substring(s.indexOf(":"),s.length()+1))
                }
                if (logFileName != null && !logFileName.equals(null)) {
                    for (int i = 0; i < folderName.length; i++) {
                        if (folderName[i].equals(logFileName)) {
                            index = i + 1
                            continue
                        }
                    }
                }
                for (int i = index; i < folderName.length; i++) {
                    // 存储解析内容
                    List<UserNotificationRecord> logList = new ArrayList<UserNotificationRecord>()
                    java.io.BufferedReader readers = new java.io.BufferedReader(
                            new java.io.FileReader(path + "/" + folderName[i]))
                    String str = readers.readLine()// reader
                    log.info("Begin parsing the log file")
                    while (str != null && !str.equals("")) {
                        UserNotificationRecord cutLogs = new UserNotificationRecord()
                        def arr = str.split("\\|")
                        //def date = arr[0]?.substring(0, 23)
                        def date=arr[0]?.substring(0, arr[0]?.indexOf(" ",arr[0]?.indexOf(" ")+1));
                        def result = (arr[1].trim() == "SUCCESS") ? 0 : 1
                        def policyName = arr[3].trim()
                        def phone = arr[4].trim()
                        def content = arr[6]
                        cutLogs.setNotifyDate(date)
                        cutLogs.setNotifyResult(result)
                        cutLogs.setNotifyPolicyName(policyName)
                        cutLogs.setSubscriberId(phone)
                        cutLogs.setNotifyContent(content)
                        //cutLogs.setNotify_type(0)
                        logList.add(cutLogs)
                        str = readers.readLine()
                    }
                    readers.close()
                    log.info("End parsing the log file")
                    log.info("Data is stored in the database")
                    // Data is stored in the database
                    logList.each {
                        println it.properties
                        it.save()
                        println it.errors
                    }
                    log.info("End of memory")
                    PrintWriter pw = new PrintWriter(new FileWriter(LogIndex))
                    pw.print(folderName[i] + ":")
                    pw.close()
                }
                //开始备份下载的cmpp文件到backup中
                String newPath= path.substring(0, path.lastIndexOf("/"))+"/backup"//linux
                //String newPath = path.substring(0, path.lastIndexOf("\\"))+"/backup"//windows
                copyFolder(path,newPath,folderName)
            }
        }
        //timer
        long intevalPeriod = minute * 60000// 定时器时间
        Timer timer = new Timer()
        long delay = 0
        timer.scheduleAtFixedRate(task, delay, intevalPeriod)
    }

}
