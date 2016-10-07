package org.dmc.services.projects;

import static org.junit.Assert.*;

import org.dmc.services.member.FollowingMember;
import org.junit.Test;

public class ProjectMemberTest {

    @Test
    public void testSetId() {
        final ProjectMember item = new ProjectMember();
        final String sampleId = "123-456-789";
        item.setId(sampleId);
        assertEquals("id is not correct", sampleId, item.getId());
        assertEquals("projectId is not correct","123", item.getProjectId());
        assertEquals("profileId is not correct", "456", item.getProfileId());
        assertEquals("fromProfileId is not correct", "789", item.getFromProfileId());
    }

    @Test
    public void testSetProjectProfileAndRequester() {
        ProjectMember item = new ProjectMember();
        item.setProjectId("987");
        item.setProfileId("111");
        item.setFromProfileId("222");
        assertEquals("id is not correct", "987-111-222", item.getId());
        assertEquals("projectId is not correct","987", item.getProjectId());
        assertEquals("profileId is not correct", "111", item.getProfileId());
        assertEquals("fromProfileId is not correct", "222", item.getFromProfileId());
    }

    @Test
    public void testSetAndEquals() {
        ProjectMember item = new ProjectMember();
        item.setProjectId("987");
        item.setProfileId("111");
        item.setFromProfileId("222");

        assertEquals("id is not correct", "987-111-222", item.getId());
        assertEquals("projectId is not correct","987", item.getProjectId());
        assertEquals("profileId is not correct", "111", item.getProfileId());
        assertEquals("fromProfileId is not correct", "222", item.getFromProfileId());

        ProjectMember item2 = new ProjectMember();
        item2.setProjectId("987");
        item2.setProfileId("111");
        item2.setFromProfileId("222");

        assertEquals("both items should match", item, item2);
    }
}
