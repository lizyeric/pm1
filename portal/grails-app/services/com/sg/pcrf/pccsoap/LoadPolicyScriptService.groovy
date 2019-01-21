package com.sg.pcrf.pccsoap

import com.sg.pcrf.LangUtils
import com.sg.pcrf.exceptions.CMPPException
import com.sg.pcrf.exceptions.LoadPolicyScriptException
import com.sg.pcrf.ossi.RunCommand
import com.sg.pcrf.util.Sftp.SftpDown
import com.sg.pcrf.util.Sftp.SftpUp
import grails.transaction.Transactional
import net.sf.json.JSONObject
import org.dom4j.Document
import org.dom4j.Element
import org.dom4j.io.SAXReader
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.support.PropertiesLoaderUtils

import java.text.SimpleDateFormat

/**
 * Created by ** on 2017/9/5.
 */
@Transactional
class LoadPolicyScriptService {
    def userNotificationRecordService
    def grailsApplication

    def getConfigProperties() {
        def path = grailsApplication.config.basepath
        def resource = new FileSystemResource("${path}${File.separator}config${File.separator}policyScriptConfiguration.properties")
        def properties = PropertiesLoaderUtils.loadProperties(resource)
        return properties
    }
    //发送soap消息
    def httpClientToSoap(String xml) throws IOException {
        def map = getConfigProperties()
        //开启 一个http链接
        URL url = new URL(map.get("HttpClient"))
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection()
        //设置post请求，post是大写
        httpURLConnection.setRequestMethod("POST")
        //Content-Type: text/xml; charset=utf-8
        httpURLConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8")
        //设置请求和响应
        httpURLConnection.setDoInput(true)
        httpURLConnection.setDoOutput(true)
        String requestString = this.requestString(xml)
        //发送soap协议
        httpURLConnection.getOutputStream().write(requestString.getBytes())
        //接收响应内容
        InputStream inputStream = httpURLConnection.getInputStream()
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        int len = -1
        byte[] b = new byte[1024]
        //将inputStream内容写到byteArrayOutputStream
        while ((len = inputStream.read(b, 0, 1024)) != -1) {
            byteArrayOutputStream.write(b, 0, len)
        }
        //获取响应内容
        String responseString = byteArrayOutputStream.toString()
        System.out.println(responseString)
        log.info(responseString)
        //解析响应的xml数据。
        inputStream.close()
        byteArrayOutputStream.close()
        log.info("")
    }

    /**
     * 策略脚本文件下载
     */
    def downFile(List<String> pathList, String taskId) {
        log.info("Start downloading policy script")
        SftpDown sd = new SftpDown()
        String names = null
        def map = getConfigProperties()//get cmpp.json
        def policyDownPath = map.get("localPath")//get policy script down adress
        File files = new File(policyDownPath)
        if (!files.exists()) {
            files.mkdirs()
        }
        def downPahtList = []
        for (int i = 0; i < pathList.size(); i++) {
            String filePaths = pathList.get(i)
            String[] fileAr = filePaths.split("/")
            names = fileAr[fileAr.length - 1]
            String downPath = policyDownPath + "/" + names
            try {
                //进入下载方法
                sd.sftpDownAndUp(filePaths, downPath, map.get("host"), map.get("port"), map.get("username"), map.get("password"), 0);
                log.info("End download policy script")
                this.sendDownSuccess(names, taskId)//成功是返回消息
                downPahtList.add(downPath)
            } catch (LoadPolicyScriptException e) {
                log.error(e.printStackTrace())
                //下载失败返回失败响应
                this.sendDownError(names, taskId)
            }
        }
        return downPahtList
    }

    //新增和批量新增策略脚本执行
    def executePolicyScript(String xmlPath, String policyName, String taskId) {
        log.info("Start execute policy script")
        def map = getConfigProperties()
        //String errorLogFileName = "/errorLogFIle.txt"
        String command = "python " + map.get("pyPath") + " " + map.get("URL") + " " + xmlPath;
//        String backContent = RunCommand.runCommand("python " + map.get("pyPath") + map.get("URL") + " " + policyName)
        if (LangUtils.isNotEmpty(policyName)) {
            policyName = policyName.replaceAll(" ", "");
            //policyName=policyName.trim()
            command = command + " " + policyName;
            log.info("###########" + command)
        }
        log.info("---------command-----" + command)
        String backContent = null;
        try {
             backContent = RunCommand.runCommand(command)
        } catch (Exception e) {
            e.printStackTrace()
            log.info("Policy script execute failed")
        }
        //log.info("--------------------backContent---" + backContent)
        //String str = "import [ policyExport_1703629213080.xml ] faild!\r\n[ PolicyTestRule11 ]  Policy Servers - Deployment fails\r\n"
        String[] ss = backContent.split("\n")
        println(ss)
        List<String> lists = new ArrayList<String>()
        ss.each {
           // println("---it--------" + it + "----it---------------")
            if (!it.toLowerCase().contains("succe".toLowerCase())) {
                lists.add(it)
            }
        }
        //String backContent="1"
        String policyScriptFileName = xmlPath.substring(xmlPath.lastIndexOf("/") + 1, xmlPath.length())
       // backContent = backContent.trim()
        if (!LangUtils.isListEmpty(lists)) {
            String errorLogFileName = writerLog(lists, policyScriptFileName)
            this.upFile(policyScriptFileName, errorLogFileName, taskId)
            this.executeScriptFailed(map.get("logUpPath"), policyScriptFileName, errorLogFileName, taskId)
        } else {
            this.executeScriptSuccess(policyScriptFileName, taskId)
        }
        log.info("End execute policy script")
    }
    //编写错误日志
    def writerLog(List lists, String policyName) {
        try {
            def map = getConfigProperties()
            String localPath = map.get("localPath")
            String errorlogPath = localPath.substring(0, localPath.lastIndexOf("/"))

            File file = new File(errorlogPath + "/errorLog/");
            if (!file.exists()) {
                file.mkdirs();
            }
            def arr = policyName.split("_")
            String policyCode = arr[0].trim()
            String ss = arr[1].trim()
            String prcfId = arr[2].trim()
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss")
            String date = sdf.format(new Date())
            def fi=file.getPath()+"/"+policyCode + "_ErrorLog_" + ss + "_" + prcfId + "_" + date
            File files = new File(fi);
            if (!files.exists()) {
                files.createNewFile();
            }
            FileWriter writer;
            writer = new FileWriter(files);
            for (int i = 0; i < lists.size(); i++) {
                writer.append(lists.get(i) + "\r\n");
            }
            writer.flush();
            writer.close();
            log.info("--------files.tostring----" + files.toString())
            return files.toString()
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * 错误日志上传
     */
    def upFile(String policyScriptFileName, String errorLogFileNamePath, String taskId) {
        log.info("Start up log file")
        def map = getConfigProperties()
        def errorLogFileName = errorLogFileNamePath.substring(errorLogFileNamePath.lastIndexOf("/"), errorLogFileNamePath.length())
        try {
            SftpDown sd = new SftpDown()
            //进入下载方法
            log.info("End up log file")
            sd.sftpDownAndUp(map.get("logUpPath") + errorLogFileName, errorLogFileNamePath, map.get("host"), map.get("port"), map.get("username"), map.get("password"), 1);
        } catch (Exception e) {
            this.errorLogUpFailed(policyScriptFileName, errorLogFileName, taskId, map.get("logUpPath"))
            e.printStackTrace()
        }

    }

    /**
     * xml解析
     */
    /*  def analysisPolicyFile(String xmlPath) {
          SAXReader reader = new SAXReader();
          Document document = reader.read(new File("C:\\download\\policyExport_batch.xml"));
          // 获取根节点
          Element rootElt = document.getRootElement();
          // 拿到根节点的名称
          System.out.println("根节点：" + rootElt.getName());
          // 获取根节点下的子节点
          Iterator iter = rootElt.elementIterator("Policy");
          String parValue = null;

          while (iter.hasNext()) {
              Element recordEle = (Element) iter.next();
              // 拿到attribute节点下的子节点key和value值
              String name = recordEle.elementTextTrim("Name").trim();
          }

          return returns;
      }*/
    /**
     * 下载成功发送的请求消息
     */
    public void sendDownSuccess(String PolicyScriptFileName, String TaskID) {
        log.info("Start send download successfully SOAP message")
        String sends = "<POP:ScriptDownloadResult  xmlns:POP=\"http://schemas.xmlsoap.org/soap/POP/\">\n" +
                "<inPara>\n" +
                "<BATInfo>\n" +
                "    <attribute>\n" +
                "       <key>PolicyScriptFileName</key>\n" +
                "       <value>" + PolicyScriptFileName + "</value>\n" +
                "    </attribute>\n" +
                "    <attribute>\n" +
                "       <key>Result</key>\n" +
                "       <value>Success</value>\n" +
                "    </attribute>\n" +
                "    <attribute>\n" +
                "       <key>TaskID</key>\n" +
                "       <value>" + TaskID + "</value>\n" +
                "    </attribute>\n" +
                "</BATInfo>\n" +
                "</inPara>\n" +
                "</POP:ScriptDownloadResult >"//下载成功返回成功的响应
        try {
            this.httpClientToSoap(sends)
            log.info("End send download successfully SOAP message")
        } catch (IOException e) {
            throw new LoadPolicyScriptException("send download successfully SOAP message IOException")
        }

    }

    /**
     * 下载失败发送的请求消息
     */
    public void sendDownError(String fileName, String taskId) {
        log.info("Start send download failed SOAP message")
        String sendError = "<POP:ScriptDownloadResult xmlns:POP=\"http://schemas.xmlsoap.org/soap/POP/\">  \n" +
                "  <inPara> \n" +
                "    <BATInfo> \n" +
                "      <attribute> \n" +
                "        <key>PolicyScriptFileName</key>  \n" +
                "        <value>" + fileName + "</value> \n" +
                "      </attribute>  \n" +
                "      <attribute> \n" +
                "        <key>Result</key>  \n" +
                "        <value>Script Download Fail</value> \n" +
                "      </attribute>  \n" +
                "      <attribute> \n" +
                "        <key>TaskID</key>  \n" +
                "        <value>" + taskId + "</value> \n" +
                "      </attribute> \n" +
                "    </BATInfo> \n" +
                "  </inPara> \n" +
                "</POP:ScriptDownloadResult>"
        try {
            this.httpClientToSoap(sendError)
            log.info("End send download failed SOAP message")
        } catch (IOException e) {
            throw new LoadPolicyScriptException("send download failed SOAP message IOException")
        }
    }
    //脚本执行结果(失败)
    def executeScriptFailed(String FilePath, String PolicyScriptFileName, String ErrorLogFileName, String taskId) {
        log.info("Start send execute script failed SOAP message")
        String xmlStr = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:POP=\"POP:soap\">  \n" +
                "  <soapenv:Header/>  \n" +
                "  <soapenv:Body> \n" +
                "    <POP:PolicyResult> \n" +
                "      <inPara> \n" +
                "        <BATInfo> \n" +
                "          <attribute> \n" +
                "            <key>PolicyScriptFileName</key>  \n" +
                "            <value>" + PolicyScriptFileName + "</value> \n" +
                "          </attribute>  \n" +
                "          <attribute> \n" +
                "            <key>Result</key>  \n" +
                "            <value>Fail</value> \n" +
                "          </attribute>  \n" +
                "          <attribute> \n" +
                "            <key>ErrorLogFileName</key>  \n" +
                "            <value>" + ErrorLogFileName + "</value> \n" +
                "          </attribute>  \n" +
                "          <attribute> \n" +
                "            <key>FilePath</key>  \n" +
                "            <value>" + FilePath + "</value> \n" +
                "          </attribute>  \n" +
                "          <attribute> \n" +
                "            <key>TaskID</key>  \n" +
                "            <value>" + taskId + "</value> \n" +
                "          </attribute> \n" +
                "        </BATInfo> \n" +
                "      </inPara> \n" +
                "    </POP:PolicyResult> \n" +
                "  </soapenv:Body> \n" +
                "</soapenv:Envelope>"
        try {
            this.httpClientToSoap(xmlStr)
            log.info("End send execute script failed SOAP message")
        } catch (IOException e) {
            throw new LoadPolicyScriptException("send execute script failed SOAP message IOException")
        }
    }

    //脚本执行结果(成功)
    def executeScriptSuccess(String PolicyScriptFileName, String taskId) {
        log.info("Start send execute script success SOAP message")
        String xmlStr = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:POP=\"POP:soap\">  \n" +
                "  <soapenv:Header/>  \n" +
                "  <soapenv:Body> \n" +
                "    <POP:PolicyResult> \n" +
                "      <inPara> \n" +
                "        <BATInfo> \n" +
                "          <attribute> \n" +
                "            <key>PolicyScriptFileName</key>  \n" +
                "            <value>" + PolicyScriptFileName + "</value> \n" +
                "          </attribute>  \n" +
                "          <attribute> \n" +
                "            <key>Result</key>  \n" +
                "            <value>Success</value> \n" +
                "          </attribute>  \n" +
                "          <attribute> \n" +
                "            <key>TaskID</key>  \n" +
                "            <value>" + taskId + "</value> \n" +
                "          </attribute> \n" +
                "        </BATInfo> \n" +
                "      </inPara> \n" +
                "    </POP:PolicyResult> \n" +
                "  </soapenv:Body> \n" +
                "</soapenv:Envelope>"
        try {
            this.httpClientToSoap(xmlStr)
            log.info("End send execute script success SOAP message")
        } catch (IOException e) {
            throw new LoadPolicyScriptException("send download failed SOAP message IOException")
        }
    }

    //错误日志上传失败
    def errorLogUpFailed(String policyScriptFileName, String errorLogFileName, String taskId, String upPath) {
        log.info("Start send error log up failed SOAP message")
        String xmlStr = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:POP=\"POP:soap\">  \n" +
                "  <soapenv:Header/>  \n" +
                "  <soapenv:Body> \n" +
                "    <POP:PolicyResult> \n" +
                "      <inPara> \n" +
                "        <BATInfo> \n" +
                "          <attribute> \n" +
                "            <key>PolicyScriptFileName</key>  \n" +
                "            <value>" + policyScriptFileName + "</value> \n" +
                "          </attribute>  \n" +
                "          <attribute> \n" +
                "            <key>Result</key>  \n" +
                "            <value>Log Upload Fail</value> \n" +
                "          </attribute>  \n" +
                "          <attribute> \n" +
                "            <key>ErrorLogFileName</key>  \n" +
                "            <value>" + errorLogFileName + "</value> \n" +
                "          </attribute>  \n" +
                "          <attribute> \n" +
                "            <key>FilePath</key>  \n" +
                "            <value>" + upPath + "</value> \n" +
                "          </attribute>  \n" +
                "          <attribute> \n" +
                "            <key>TaskID</key>  \n" +
                "            <value>" + taskId + "</value> \n" +
                "          </attribute> \n" +
                "        </BATInfo> \n" +
                "      </inPara> \n" +
                "    </POP:PolicyResult> \n" +
                "  </soapenv:Body> \n" +
                "</soapenv:Envelope>"
        try {
            this.httpClientToSoap(xmlStr)
            log.info("End send error log upload failed message")
        } catch (IOException e) {
            e.printStackTrace()
            throw new LoadPolicyScriptException("send error log upload failed SOAP message IOException")
        }
    }
    //soap协议内容，请求的 内容
    def requestString(String cityName) {
        String xmlString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + cityName
        return xmlString
    }

}
