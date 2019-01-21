package soap

interface BaseSoapService {
//  retrieve necessary data from database
    def retrieveData(parsedXml)
//  construct thr SQL for needed data
    def makeQueryString(String startTime, String endTime, String subId)
//  make the single User xml using the subscriber id and the record list.
    def makeSingleUserXml(subId, queryResultList)
//  append headers to the partial xml
    def appendResponse(xml)
}
