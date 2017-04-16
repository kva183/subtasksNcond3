package ut.com.thisworld.jira.plugin.jira.workflow;

import com.thisworld.jira.plugin.jira.workflow.TwWorkflowCondition;

import com.atlassian.jira.issue.MutableIssue;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import com.atlassian.jira.issue.Issue;
//import java.util.*;
import com.atlassian.jira.mock.component.MockComponentWorker;
import org.mockito.Mock;
//import com.atlassian.jira.mock.*;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.SubTaskManager;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.config.MockSubTaskManager;

public class TwWorkflowConditionTest
{
    public static final String FIELD_WORD = "word";
    public static final String FIELD_SUBTASK_QTY = "subtasks_qty";
    public static final String FIELD_SUBTASK_TYPE_ID = "subtask_type_id";
    private final static String ISSUE_TYPE1 = "111";

    protected TwWorkflowCondition condition;
    protected MutableIssue issue;
    protected Issue subtask1;
    protected Issue subtask2;
    protected Issue subtask3;
    protected Collection<Issue> subtasks;
    protected IssueType issueType1;
    protected MockSubTaskManager mockSubTaskManager;

    @Mock
    private MockComponentWorker mw;
	
    @Before
    public void setup() {
        mw = new MockComponentWorker()
		.init();
	ComponentAccessor.initialiseWorker(mw);
	mockSubTaskManager = mock(MockSubTaskManager.class);

	issue = mock(MutableIssue.class);
	subtask1 = mock(MutableIssue.class);
        subtask2 = mock(MutableIssue.class);
        subtask3 = mock(MutableIssue.class);
	subtasks = new ArrayList<Issue>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        subtasks.add(subtask3);
	issueType1 = mock(IssueType.class);

        condition = new TwWorkflowCondition() {
            protected MutableIssue getIssue(Map transientVars) {
                return issue;
            }
        };
    }

    @Test
    public void testPassesCondition() throws Exception
    {
        Map transientVars = new HashMap();
        transientVars.put(FIELD_SUBTASK_QTY, "3");
        transientVars.put(FIELD_SUBTASK_TYPE_ID, "111");
        Map args = new HashMap();
        args.put(FIELD_SUBTASK_QTY, "3");
        args.put(FIELD_SUBTASK_TYPE_ID, "111");
	when(issueType1.getId()).thenReturn(ISSUE_TYPE1);
	when(issue.getSubTaskObjects()).thenReturn(subtasks);
	when(subtask1.getIssueTypeObject()).thenReturn(issueType1);
	when(subtask2.getIssueTypeObject()).thenReturn(issueType1);
	when(subtask3.getIssueTypeObject()).thenReturn(issueType1);
	when(mockSubTaskManager.isSubTasksEnabled()).thenReturn(true);

        boolean result = condition.passesCondition(transientVars, args, null);

        assertTrue("condition should pass", result);
    }

    @Test
    public void testFailsCondition() throws Exception
    {
        Map transientVars = new HashMap();
        transientVars.put(FIELD_SUBTASK_QTY, "1");
        Map args = new HashMap();
        args.put(FIELD_SUBTASK_QTY, "1");
        when(issue.getSubTaskObjects()).thenReturn(null);
	when(mockSubTaskManager.isSubTasksEnabled()).thenReturn(true);

        boolean result = condition.passesCondition(transientVars, args, null);

        assertFalse("condition should fail", result);
    }

}
