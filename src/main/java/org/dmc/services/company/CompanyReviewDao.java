package org.dmc.services.company;

import org.dmc.services.*;
import org.dmc.services.utils.SQLUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.ws.http.HTTPException;
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

            String query =
                    "select r.*, o.accountid AS accountid from organization_review r LEFT JOIN organization o ON o.organization_id = r.organization_id " +
                    whereClause;

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
                r.setStatus(resultSet.getBoolean("status"));
                String dateStr = resultSet.getString("date");
                r.setRating(resultSet.getInt("rating"));
                r.setLike(resultSet.getInt("likes"));
                r.setDislike(resultSet.getInt("dislike"));
                r.setComment(resultSet.getString("comment"));

                // account_id is associated with organization table:  accountId integer
                r.setAccountId(Integer.toString(resultSet.getInt("accountid")));

                if (reviews == null) reviews = new ArrayList<CompanyReview>();

                reviews.add(r);
            }

        } catch (SQLException ex) {
            throw new DMCServiceException(DMCError.OtherSQLError, "Error: " + ex.toString());
        }

        return reviews;
    }

    public Id createCompanyReview (CompanyReview companyReview, String userEPPN) throws DMCServiceException {

        int id = -1;
        String query = "INSERT INTO organization_review (organization_id, name, reply, reviewId, status, date, rating, likes, dislike, comment) VALUES (?,?,?,?,?,?,?,?,?,?)";

        try {

            if (!CompanyUserUtil.isDMDIIMember(userEPPN)) {
                ServiceLogger.log(logTag, "User: " + userEPPN + " is not DMDII Member");
                throw new DMCServiceException(DMCError.NotDMDIIMember, "User " + userEPPN + " is not a member of the DMDII");
            }

            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(companyReview.getCompanyId()));
            preparedStatement.setString(2, companyReview.getName());
            preparedStatement.setBoolean(3, companyReview.getReply());
            preparedStatement.setString(4, companyReview.getReviewId());
            preparedStatement.setBoolean(5, companyReview.getStatus());
            preparedStatement.setString(6,companyReview.getDate().toString());
            preparedStatement.setInt(7, companyReview.getRating());
            preparedStatement.setInt(8, companyReview.getLike());
            preparedStatement.setInt(9, companyReview.getDislike());
            preparedStatement.setString(10, companyReview.getComment());

            int rCreate = preparedStatement.executeUpdate();

            String queryId = "select max(id) max_id from organization_review";
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
