package org.dmc.services.company;

import org.dmc.services.*;
import org.dmc.services.utils.SQLUtils;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 200005921 on 6/9/2016.
 */
public class CompanyReviewDao {

    private final String logTag = CompanyReviewDao.class.getName();
    private ResultSet resultSet;

    public List<CompanyReview> getReviews (int companyId,
                                           String reviewId,
                                           Integer limit,
                                           String order,
                                           String sort,
                                           Integer rating,
                                           Boolean status,
                                           String userEPPN)
            throws DMCServiceException {
        List<CompanyReview> reviews = new ArrayList<CompanyReview>();

        try {

            if (!CompanyUserUtil.isDMDIIMember(userEPPN)) {
                ServiceLogger.log(logTag, "User: " + userEPPN + " is not DMDII Member");
                throw new DMCServiceException(DMCError.NotDMDIIMember, "User " + userEPPN + " is not a member of the DMDII");
            }

            String whereClause = " WHERE r.organization_id = ?";
            int numFields = 1;
            int reviewIdIndex = -1;
            int ratingIndex = -1;
            int statusIndex = -1;

            int reviewIdVal = 0;


            // reviewId > = indicates a specific reviewId, otherwise retrieve all reviews
            if (reviewId != null && reviewId.trim().length() > 0) {

                try {
                    reviewIdVal = Integer.parseInt(reviewId.toString());

                    if (reviewIdVal > 0) {
                        reviewIdIndex = ++numFields;
                        whereClause += " AND r.id = ?";
                        ServiceLogger.log(logTag, "reviewId index is " + reviewIdIndex + ", value is " + reviewId);
                    }
                } catch (NumberFormatException nfe) {
                    ServiceLogger.log(logTag, "Error parsing review id: " + reviewId + ": " + nfe.toString());
                }
            }

            if (rating != null) {
                ratingIndex = ++numFields;
                whereClause += " AND r.rating = ?";
                ServiceLogger.log(logTag, "rating index is " + ratingIndex + ", value is " + rating);
            }

            if (status != null) {
                statusIndex = ++numFields;
                whereClause += " AND r.status = ?";
                ServiceLogger.log(logTag, "status index is " + statusIndex + ", value is " + status);
            }

            // "ORDER BY " + sort + " " + order + " LIMIT " + limit;
            String orderByClause = SQLUtils.buildOrderByClause(order, sort);
            String limitClause = SQLUtils.buildLimitClause(limit);

//            String query =
//                    "select r.*, o.accountid AS accountid from organization_review r LEFT JOIN organization o ON o.organization_id = r.organization_id " +
//                    whereClause;

            String query =
                "Select r.id, r.organization_id, u.realname as name, r.user_id as accountId, review_timestamp , r.review as comment, r.stars as rating, count(RR.*) AS count_helpfulOrNot " +
                "FROM organization_review_new r " +
                "LEFT JOIN organization  o ON  r.organization_id  = o.organization_id " +
                "LEFT JOIN organization_review_rate RR on RR.review_id = r.id " +
                "LEFT JOIN users u ON u.user_id = r.user_id " +
                whereClause +
                " GROUP BY o.name, r.id, u.realname";

            if (orderByClause != null) {
                query += " " + orderByClause;
            }

            if (limitClause != null) {
                query += limitClause;
            }

            ServiceLogger.log(logTag, "Get company reviews sql=" + query);

            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.setInt(1, companyId);
            if (reviewIdIndex != -1) preparedStatement.setInt(reviewIdIndex, reviewIdVal);
            if (ratingIndex != -1) preparedStatement.setInt(ratingIndex, rating);
            if (statusIndex != -1) preparedStatement.setBoolean(statusIndex, status);

            this.resultSet = preparedStatement.executeQuery();
            while (this.resultSet.next()) {
                CompanyReview r = new CompanyReview();

                //                id serial primary key,
                //                organization_id integer,
                //                name text,
                //                reply boolean,
                //                reviewId text,
                //                status boolean,
                //                date text,
                //                rating integer,
                //                likes integer,
                //                dislike integer,
                //                comment text

                r.setId(Integer.toString(resultSet.getInt("id")));
                r.setCompanyId(Integer.toString(resultSet.getInt("organization_id")));
                r.setName(resultSet.getString("name"));
                r.setReviewId(reviewId);
                java.sql.Timestamp reviewTimestamp = resultSet.getTimestamp("review_timestamp");
                BigDecimal bdDate = new BigDecimal(reviewTimestamp.getTime());
                r.setDate(bdDate);
                r.setRating(resultSet.getInt("rating"));
                r.setComment(resultSet.getString("comment"));

                // set to true if review has a reply?
                int count_replies = countRepliesForReview(Integer.parseInt(reviewId));
                boolean hasRplies = (count_replies > 0);
                r.setReply(hasRplies);

                //r.setStatus(resultSet.getBoolean("status"));
                r.setStatus(true);

                int count_likes = countHelpfulForReview(Integer.parseInt(reviewId), true);
                r.setLike(count_likes);

                int count_dislikes = countHelpfulForReview(Integer.parseInt(reviewId), false);
                r.setDislike(count_dislikes);

                // account_id is associated with organization table:  accountId integer
                r.setAccountId(Integer.toString(resultSet.getInt("accountId")));

                if (reviews == null) reviews = new ArrayList<CompanyReview>();

                reviews.add(r);
            }

        } catch (SQLException ex) {
            throw new DMCServiceException(DMCError.OtherSQLError, "Error: " + ex.toString());
        }

        return reviews;
    }

    public List<CompanyReview> getReviewReplies (int companyId,
                                           String reviewId,
                                           Integer limit,
                                           String order,
                                           String sort,
                                           Integer rating,
                                           Boolean status,
                                           String userEPPN)
            throws DMCServiceException {
        List<CompanyReview> reviews = new ArrayList<CompanyReview>();

        try {

            if (!CompanyUserUtil.isDMDIIMember(userEPPN)) {
                ServiceLogger.log(logTag, "User: " + userEPPN + " is not DMDII Member");
                throw new DMCServiceException(DMCError.NotDMDIIMember, "User " + userEPPN + " is not a member of the DMDII");
            }

            String whereClause = " WHERE r.organization_id = ?";
            int numFields = 1;
            int reviewIdIndex = -1;
            int ratingIndex = -1;
            int statusIndex = -1;

            int reviewIdVal = 0;

            // reviewId > = indicates a specific reviewId, otherwise retrieve all reviews
            if (reviewId != null && reviewId.trim().length() > 0) {

                try {
                    reviewIdVal = Integer.parseInt(reviewId.toString());

                    if (reviewIdVal > 0) {
                        reviewIdIndex = ++numFields;
                        whereClause += " AND r.id = ?";
                        ServiceLogger.log(logTag, "reviewId index is " + reviewIdIndex + ", value is " + reviewId);
                    }
                } catch (NumberFormatException nfe) {
                    ServiceLogger.log(logTag, "Error parsing review id: " + reviewId + ": " + nfe.toString());
                }
            }

            if (reviewIdVal <= 0) {
                ServiceLogger.log(logTag, "review_id must be greater than 0 to retrieve company review replies");
                throw new DMCServiceException(DMCError.OtherSQLError, "review_id must be greater than 0 to retrieve company review replies");

            }

//            if (rating != null) {
//                ratingIndex = ++numFields;
//                whereClause += " AND r.rating = ?";
//                ServiceLogger.log(logTag, "rating index is " + ratingIndex + ", value is " + rating);
//            }

//            if (status != null) {
//                statusIndex = ++numFields;
//                whereClause += " AND r.status = ?";
//                ServiceLogger.log(logTag, "status index is " + statusIndex + ", value is " + status);
//            }

            // "ORDER BY " + sort + " " + order + " LIMIT " + limit;
            String orderByClause = SQLUtils.buildOrderByClause(order, sort);
            String limitClause = SQLUtils.buildLimitClause(limit);

            String query =
                "select r.id, r.review_id AS reviewId, rn.organization_id as organization_id, u.realname as name, r.user_id as accountId, r.review_reply_timestamp , r.review_reply as comment, count(RR.*) AS count_helpfulOrNot " +
                "FROM organization_review_reply r " +
                "LEFT JOIN organization_review_new rn ON rn.id = r.review_id " +
                "LEFT JOIN organization  o ON  rn.organization_id  = o.organization_id " +
                "LEFT JOIN organization_review_reply_rate RR on RR.review_reply_id = r.id " +
                "LEFT JOIN users u ON u.user_id = r.user_id " +
                whereClause +
                " GROUP BY rn.organization_id, o.name, r.id, u.realname";

            if (orderByClause != null) {
                query += " " + orderByClause;
            }

            if (limitClause != null) {
                query += limitClause;
            }

            ServiceLogger.log(logTag, "Get company reviews sql=" + query);

            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.setInt(1, companyId);
            if (reviewIdIndex != -1) preparedStatement.setInt(reviewIdIndex, reviewIdVal);
            if (ratingIndex != -1) preparedStatement.setInt(ratingIndex, rating);
            if (statusIndex != -1) preparedStatement.setBoolean(statusIndex, status);

            this.resultSet = preparedStatement.executeQuery();
            while (this.resultSet.next()) {
                CompanyReview r = new CompanyReview();

                //                id serial primary key,
                //                organization_id integer,
                //                name text,
                //                reply boolean,
                //                reviewId text,
                //                status boolean,
                //                date text,
                //                rating integer,
                //                likes integer,
                //                dislike integer,
                //                comment text

                r.setId(Integer.toString(resultSet.getInt("id")));
                r.setCompanyId(Integer.toString(resultSet.getInt("organization_id")));
                r.setName(resultSet.getString("name"));
                r.setReply(resultSet.getBoolean("reply"));
                r.setReviewId(resultSet.getString("reviewId"));
                java.sql.Timestamp reviewTimestamp = resultSet.getTimestamp("review_reply_timestamp");
                BigDecimal bdDate = new BigDecimal(reviewTimestamp.getTime());
                r.setDate(bdDate);
                r.setRating(resultSet.getInt("rating"));
                r.setComment(resultSet.getString("comment"));

                // replies do not have replies
                boolean hasRplies = false;
                r.setReply(hasRplies);

                //r.setStatus(resultSet.getBoolean("status"));
                r.setStatus(true);

                int count_likes = countHelpfulForReviewReply(Integer.parseInt(reviewId), true);
                r.setLike(count_likes);

                int count_dislikes = countHelpfulForReviewReply(Integer.parseInt(reviewId), false);
                r.setDislike(count_dislikes);

                // account_id is associated with organization table:  accountId integer
                r.setAccountId(Integer.toString(resultSet.getInt("accountId")));

                if (reviews == null) reviews = new ArrayList<CompanyReview>();

                reviews.add(r);
            }

        } catch (SQLException ex) {
            throw new DMCServiceException(DMCError.OtherSQLError, "Error: " + ex.toString());
        }

        return reviews;
    }

    public int countRepliesForReview (int reviewId) {
        String q = "select count(*) FROM organization_review_reply WHERE review_id = " + reviewId;
        int count = 0;
        ResultSet rs = DBConnector.executeQuery(q);
        try {
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException sqlEX) {
            // ignore
        }
        return count;
    }

    public int countHelpfulForReview (int reviewId, boolean helpfulOrNot) {
        String q = "select count(*) FROM organization_review_rate WHERE review_id = " + reviewId + " AND helpfulOrNot IS " + Boolean.toString(helpfulOrNot);
        int count = 0;
        ResultSet rs = DBConnector.executeQuery(q);
        try {
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException sqlEX) {
            // ignore
        }
        return count;
    }

    public int countHelpfulForReviewReply (int reviewReplyId, boolean helpfulOrNot) {
        String q = "select count(*) FROM organization_review_reply_rate WHERE review_reply_id = " + reviewReplyId + " AND helpfulOrNot IS " + Boolean.toString(helpfulOrNot);
        int count = 0;
        ResultSet rs = DBConnector.executeQuery(q);
        try {
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException sqlEX) {
            // ignore
        }
        return count;
    }

    public Id createCompanyReview (CompanyReview companyReview, String userEPPN) throws DMCServiceException {

        int id = -1;
        //String query = "INSERT INTO organization_review (organization_id, name, reply, reviewId, status, date, rating, likes, dislike, comment) VALUES (?,?,?,?,?,?,?,?,?,?)";

        // organization_id, user_id, review_timestamp, review, stars
        String sqlInsertReview = "INSERT INTO organization_review_new (organization_id, user_id, review_timestamp, review, stars) VALUES (?,?,?,?,?)";

        // user_id integer, review_reply_timestamp timestamp, review_id integer, review_reply text
        String sqlInsertReply = "INSERT INTO organization_review_reply (user_id, review_reply_timestamp, review_id, review_reply) VALUES (?,?,?,?)";

        try {

            if (!CompanyUserUtil.isDMDIIMember(userEPPN)) {
                ServiceLogger.log(logTag, "User: " + userEPPN + " is not DMDII Member");
                throw new DMCServiceException(DMCError.NotDMDIIMember, "User " + userEPPN + " is not a member of the DMDII");
            }

//            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
//            preparedStatement.setInt(1, Integer.parseInt(companyReview.getCompanyId()));
//            preparedStatement.setString(2, companyReview.getName());
//            preparedStatement.setBoolean(3, companyReview.getReply());
//            preparedStatement.setString(4, companyReview.getReviewId());
//            preparedStatement.setBoolean(5, companyReview.getStatus());
//            preparedStatement.setString(6,companyReview.getDate().toString());
//            preparedStatement.setInt(7, companyReview.getRating());
//            preparedStatement.setInt(8, companyReview.getLike());
//            preparedStatement.setInt(9, companyReview.getDislike());
//            preparedStatement.setString(10, companyReview.getComment());

            PreparedStatement preparedStatement = null;
            if (companyReview.getReply()) {
                // Insert into organization_review_reply
                preparedStatement = DBConnector.prepareStatement(sqlInsertReply);
                preparedStatement.setInt(1, Integer.parseInt(companyReview.getAccountId()));
                preparedStatement.setTimestamp(2, new java.sql.Timestamp(companyReview.getDate().longValue()));
                preparedStatement.setInt(3, Integer.parseInt(companyReview.getAccountId()));
                preparedStatement.setString(4, companyReview.getComment());

            } else {
                // Insert into organization_review_new
                preparedStatement = DBConnector.prepareStatement(sqlInsertReview);
                preparedStatement.setInt(1, Integer.parseInt(companyReview.getCompanyId()));
                preparedStatement.setInt(2, Integer.parseInt(companyReview.getAccountId()));
                preparedStatement.setTimestamp(3, new java.sql.Timestamp(companyReview.getDate().longValue()));
                preparedStatement.setString(4, companyReview.getComment());
                preparedStatement.setInt(5, companyReview.getRating().intValue());
            }


            int rCreate = preparedStatement.executeUpdate();

            String queryId = "select max(id) max_id from organization_review_new";
            PreparedStatement preparedStatement1 = DBConnector.prepareStatement(queryId);
            ResultSet r=preparedStatement1.executeQuery();
            r.next();
            id=r.getInt("max_id");

        } catch (SQLException sqlEx) {
            throw new DMCServiceException(DMCError.OtherSQLError, sqlEx.toString());
        }

        return new Id.IdBuilder(id).build();
    }
 }
