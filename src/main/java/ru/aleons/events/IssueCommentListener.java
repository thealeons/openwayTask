package ru.aleons.events;

import com.atlassian.event.api.EventListener;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.issue.comments.Comment;
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.issue.comments.MutableComment;
import ru.aleons.logic.EditCommentManager;


public class IssueCommentListener {

    private final CommentManager commentManager;

    public IssueCommentListener() {
        this.commentManager = ComponentAccessor.getComponent(CommentManager.class);
    }

    @EventListener
    public void onCommentEvent(IssueEvent issueEvent) {
        Long eventTypeId = issueEvent.getEventTypeId();
        if (eventTypeId.equals(EventType.ISSUE_COMMENTED_ID) || eventTypeId.equals(EventType.ISSUE_COMMENT_EDITED_ID)) {
            Comment comment = issueEvent.getComment();
            EditCommentManager editCommentManager = new EditCommentManager();
            String newCommentText = editCommentManager.checkBodyByLink(comment.getBody());
            MutableComment newComment =commentManager.getMutableComment(comment.getId());
            newComment.setBody(newCommentText);
            commentManager.update(newComment, false);
        }


    }
}
