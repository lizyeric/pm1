package com.sg.pcrf.ossi

class SpecificQuota {

    static constraints = {
        name(nullable: false, unique: false)
        totalVolumeLimit(nullable: true)
    }

    static mapping = {
        autoTimestamp(false)
    }

    String name
    String totalVolumeLimit
    Date dateCreated
    Date lastUpdated

    String toString() {
        "quota_name: $name, total_volume_limit: $totalVolumeLimit\ncreated: $dateCreated, updated: $lastUpdated"
    }
}
