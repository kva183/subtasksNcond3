package com.thisworld.jira.plugin.jira.workflow;

import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginConditionFactory;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.ConditionDescriptor;

import java.util.HashMap;
import java.util.Map;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.issuetype.IssueType;

import com.atlassian.jira.config.ConstantsManager;

import java.util.*;

/**
 * This is the factory class responsible for dealing with the UI for the post-function.
 * This is typically where you put default values into the velocity context and where you store user input.
 */

public class TwWorkflowConditionFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginConditionFactory
{
    public static final String FIELD_SUBTASK_QTY = "subtasks_qty";
    public static final String FIELD_SUBTASK_TYPE_ID = "subtask_type_id";
    public static final String FIELD_SUBTASK_TYPE_NAME = "subtask_type_name";

    protected void getVelocityParamsForInput(Map velocityParams)
    {
        //the default value
        velocityParams.put(FIELD_SUBTASK_QTY, "1");

        //find all available Sut-task issue types
	Collection<IssueType> allIssueTypes = ComponentAccessor.getConstantsManager().getAllIssueTypeObjects();
	Collection<IssueType> subtaskIssueTypes = new ArrayList<IssueType>();

	Iterator<IssueType> itallIssueTypes = allIssueTypes.iterator();
	while( itallIssueTypes.hasNext() ) {
	  IssueType foo = itallIssueTypes.next();
	  if( foo.isSubTask() ) subtaskIssueTypes.add(foo);
	}

        velocityParams.put("subtaskIssueTypes", Collections.unmodifiableCollection(subtaskIssueTypes));
    }

    protected void getVelocityParamsForEdit(Map velocityParams, AbstractDescriptor descriptor)
    {
        getVelocityParamsForInput(velocityParams);
        getVelocityParamsForView(velocityParams, descriptor);
    }

    protected void getVelocityParamsForView(Map velocityParams, AbstractDescriptor descriptor)
    {
        if (!(descriptor instanceof ConditionDescriptor))
        {
            throw new IllegalArgumentException("Descriptor must be a ConditionDescriptor.");
        }

        ConditionDescriptor conditionDescriptor = (ConditionDescriptor)descriptor;
        velocityParams.put(FIELD_SUBTASK_QTY, conditionDescriptor.getArgs().get(FIELD_SUBTASK_QTY));
        velocityParams.put(FIELD_SUBTASK_TYPE_ID, conditionDescriptor.getArgs().get(FIELD_SUBTASK_TYPE_ID));
        //get type name for selected IssueType
	if ( conditionDescriptor.getArgs().get(FIELD_SUBTASK_TYPE_ID) != null ) {
		velocityParams.put(FIELD_SUBTASK_TYPE_NAME, 
		  ComponentAccessor.getConstantsManager().getIssueTypeObject( conditionDescriptor.getArgs().get(FIELD_SUBTASK_TYPE_ID).toString() ).getName() );
	}
    }

    public Map getDescriptorParams(Map conditionParams)
    {
        // Process The map
        Map params = new HashMap();
        params.put(FIELD_SUBTASK_QTY, extractSingleParam(conditionParams, FIELD_SUBTASK_QTY));
        params.put(FIELD_SUBTASK_TYPE_ID, extractSingleParam(conditionParams, FIELD_SUBTASK_TYPE_ID));
        return params;
    }
}
