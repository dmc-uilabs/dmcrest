package org.dmc.services.projects;

import static org.junit.Assert.*;

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

}
