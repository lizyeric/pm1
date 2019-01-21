package entity

/**
 * Policy record for policy query
 * distinguish it from Policy query which belongs to the user policy query.
 */
class PolicyAccount {
    String DEFAULT_VALUE = "0"
    String subscriber = DEFAULT_VALUE
    String time = DEFAULT_VALUE
    String usrGrade = DEFAULT_VALUE
    String location = DEFAULT_VALUE
    String destinationAddress = DEFAULT_VALUE
    String quota = DEFAULT_VALUE
    String terminalType = DEFAULT_VALUE

    String toString(){
        "policyRecord-policyQuery:\n" +
                "subscriber:$subscriber, time:$time, usrGrade:$usrGrade, location:$location, destinationAddress:$destinationAddress, quota:$quota, terminalType:$terminalType"
    }
}
