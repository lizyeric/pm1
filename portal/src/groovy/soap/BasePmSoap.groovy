package soap

import com.sg.pcrf.exceptions.PmSoapException
import org.dom4j.Document
import org.dom4j.Element
import org.dom4j.io.SAXReader

trait BasePmSoap {
    String getSoapXml(request) {
//        return raw soap message
        return request.reader.text
    }

    def parseXml(soapRequest) {
//        parse soap message and return a map.
        def node = new XmlParser().parseText(soapRequest)
        def result = node.depthFirst().get(4)
        if (result.name() != "inPara") {
            throw PmSoapException("000002:request format error")
        }
        def paramsMap = [:]
        def userList = []
        def subscriberNodes = result.User.attribute.value
        subscriberNodes.each() {
            userList.add(it.text())
        }
        paramsMap["userList"] = userList

        def attributeNodes = result.attribute
        attributeNodes.each() {
            paramsMap[it?.key?.text()] = it?.value?.text()
        }

        //get load Policy Script soap
        def batInfoNodes = result.BATInfo.attribute
        def batInfoMap = [:]
        def fileName = null
        def pathList=[]
        batInfoNodes.each{
            String key = it.key.text()
            String value = it.value.text()
            if (key.equals("PolicyScriptFileName")) {
                fileName = value
            }
            if (key.equals("PolicyScriptFilePath")) {
                String fileNamePath = value + "/" + fileName;
                fileName = null
                pathList.add(fileNamePath)
            }
            batInfoMap.put(key, value)
        }
        batInfoMap.put("pathList",pathList)
        paramsMap["BATInfo"] = batInfoMap

        return paramsMap
    }
    def parseXmlFile(String xmlPath){
        try {
            SAXReader reader = new SAXReader()
            Document document = reader.read(new File(xmlPath))
            // 获取根节点
            Element rootElt = document.getRootElement()
            // 拿到根节点的名称
            System.out.println("根节点：" + rootElt.getName())
            // 获取根节点下的子节点
            Iterator iter = rootElt.elementIterator("Policy")
             def policyNameList = []
            while (iter.hasNext()) {
                Element recordEle = (Element) iter.next()
                // 拿到attribute节点下的子节点key和value值
                String name = recordEle.elementTextTrim("Name")
                System.out.println(name)
                policyNameList.add(name)
            }
            return  policyNameList
        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}
