package portal

class MyJob {
    def execute(context) {
        println context.mergedJobDataMap.get('foo')
    }
}
