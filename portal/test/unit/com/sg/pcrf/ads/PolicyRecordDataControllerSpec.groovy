package com.sg.pcrf.ads



import grails.test.mixin.*
import spock.lang.*

@TestFor(PolicyRecordDataController)
@Mock(PolicyRecordData)
class PolicyRecordDataControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.policyRecordDataInstanceList
            model.policyRecordDataInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.policyRecordDataInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def policyRecordData = new PolicyRecordData()
            policyRecordData.validate()
            controller.save(policyRecordData)

        then:"The create view is rendered again with the correct model"
            model.policyRecordDataInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            policyRecordData = new PolicyRecordData(params)

            controller.save(policyRecordData)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/policyRecordData/show/1'
            controller.flash.message != null
            PolicyRecordData.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def policyRecordData = new PolicyRecordData(params)
            controller.show(policyRecordData)

        then:"A model is populated containing the domain instance"
            model.policyRecordDataInstance == policyRecordData
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def policyRecordData = new PolicyRecordData(params)
            controller.edit(policyRecordData)

        then:"A model is populated containing the domain instance"
            model.policyRecordDataInstance == policyRecordData
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/policyRecordData/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def policyRecordData = new PolicyRecordData()
            policyRecordData.validate()
            controller.update(policyRecordData)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.policyRecordDataInstance == policyRecordData

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            policyRecordData = new PolicyRecordData(params).save(flush: true)
            controller.update(policyRecordData)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/policyRecordData/show/$policyRecordData.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/policyRecordData/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def policyRecordData = new PolicyRecordData(params).save(flush: true)

        then:"It exists"
            PolicyRecordData.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(policyRecordData)

        then:"The instance is deleted"
            PolicyRecordData.count() == 0
            response.redirectedUrl == '/policyRecordData/index'
            flash.message != null
    }
}
