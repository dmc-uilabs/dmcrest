package org.dmc.services.member;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/following_members", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class FollowingMembersController {

    private FollowingMemberDao dao = new FollowingMemberDao();

    @RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE, TEXT_HTML_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<List<FollowingMember>> followingMembersGet(
            @RequestParam(value = "accountId", required = false) String accountId,
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "profileId", required = false) String profileId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "sort", required = false) String sort, 
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {

        return new ResponseEntity<List<FollowingMember>>(dao.followingMembersGet(accountId, id, profileId, limit, start, order, sort, userEPPN), HttpStatus.OK);
    }

    @RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE, TEXT_HTML_VALUE }, method = RequestMethod.POST)
    public ResponseEntity<FollowingMember> followingMembersPost(
            @RequestBody PostFollowingMember body,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        return new ResponseEntity<FollowingMember>(dao.followingMembersPost(body, userEPPN), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", produces = { "application/json", "text/html" }, method = RequestMethod.DELETE)
    public ResponseEntity<Void> followingMembersIdDelete(@PathVariable("id") String id) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/{id}", produces = { APPLICATION_JSON_VALUE, TEXT_HTML_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<FollowingMember> followingMembersIdGet(@PathVariable("id") String id) {
        // do some magic!
        return new ResponseEntity<FollowingMember>(HttpStatus.NOT_IMPLEMENTED);
    }

}
