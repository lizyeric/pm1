package entity

class Notification {
    String time
    String policyId
    String form
    String result

    String toString() {
        "print notification: time:$time, policyId:$policyId, form:$form, result:$result"
    }
}
