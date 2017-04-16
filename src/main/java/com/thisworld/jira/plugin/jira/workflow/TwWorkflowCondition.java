package com.thisworld.jira.plugin.jira.workflow;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.workflow.condition.AbstractJiraCondition;
import com.opensymphony.module.propertyset.PropertySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.*;

//import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.config.SubTaskManager;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.issuetype.IssueType;

public class TwWorkflowCondition extends AbstractJiraCondition
{
    private static final Logger log = LoggerFactory.getLogger(TwWorkflowCondition.class);

    public static final String FIELD_SUBTASK_TYPE_ID = "subtask_type_id";
    public static final String FIELD_SUBTASK_QTY = "subtasks_qty";

    public boolean passesCondition(Map transientVars, Map args, PropertySet ps)
    {
        String subtask_type_id = (String) args.get(FIELD_SUBTASK_TYPE_ID);
//    log.warn("subtask_type_id=["+subtask_type_id+"]");
	String subtasks_qty = (String) args.get(FIELD_SUBTASK_QTY);
//    log.warn("subtasks_qty=["+subtasks_qty+"]");

	if (subtasks_qty == null ) { return false; }

//	int i_subtasks_qty = Integer.parseInt(subtasks_qty);
//    log.warn("i_subtasks_qty=["+i_subtasks_qty+"]");

        Issue issue = getIssue(transientVars);
 //       String description = issue.getDescription();

	int subtaskCount = 0;
	SubTaskManager subTaskManager = ComponentAccessor.getSubTaskManager();
	boolean subTasksEnabled = false;
	try {
	  subTasksEnabled = subTaskManager.isSubTasksEnabled();
	} catch (Exception e) {
	  subTasksEnabled = true;
	}
	Collection<Issue> subTasks = issue.getSubTaskObjects();
	if (subTasksEnabled && subTasks != null && !subTasks.isEmpty() ) {
//	    log.warn(subTasks.size() + " subtasks found");
	    for (Issue subTask1 : subTasks) {
	        if ( subTask1.getIssueTypeObject().getId().equals( subtask_type_id ) )  {
//		   log.warn("incrementing...");
			subtaskCount++;
		}
	    }
	}

//    log.warn("subtasks of type ["+subtask_type_id+"] found: "+subtaskCount);
        return subtaskCount >= Integer.parseInt(subtasks_qty);
    }
}
