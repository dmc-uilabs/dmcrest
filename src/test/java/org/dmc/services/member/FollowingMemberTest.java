package org.dmc.services.member;

import static org.junit.Assert.*;

import org.junit.Test;

public class FollowingMemberTest {

    @Test
    public void testSetId() {
        FollowingMember item = new FollowingMember();
        final String sampleId = "123-456";
        item.setId(sampleId);
        assertEquals("id is not correct", sampleId, item.getId());
        assertEquals("follower is not correct","123", item.getFollower());
        assertEquals("followed is not correct", "456", item.getFollowed());
    }

    @Test
    public void testSetFollowerAndFollowed() {
        FollowingMember item = new FollowingMember();
        item.setFollower("987");
        item.setFollowed("111");
        assertEquals("id is not correct", "987-111", item.getId());
        assertEquals("follower is not correct","987", item.getFollower());
        assertEquals("followed is not correct", "111", item.getFollowed());
    }

    @Test
    public void testSetAndEquals() {
        FollowingMember item = new FollowingMember();
        item.setFollower("444");
        item.setFollowed("111");

        FollowingMember item2 = new FollowingMember();
        item2.setFollower("444");
        item2.setFollowed("111");
        assertEquals("id is not correct", "444-111", item.getId());
        assertEquals("follower is not correct","444", item.getFollower());
        assertEquals("followed is not correct", "111", item.getFollowed());

        assertEquals("both items should match", item, item2);
    }
}
