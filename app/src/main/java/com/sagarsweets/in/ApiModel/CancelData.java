package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancelData {
    @SerializedName("customer_reson")
    @Expose
    private String customerReason;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("approved_description")
    @Expose
    private String approvedDescription;

    @SerializedName("return_date")
    @Expose
    private String returnDate;

    @SerializedName("cancelled_by")
    @Expose
    private String cancelledBy;



    @SerializedName("refund_id")
    @Expose
    private String refundId;



    @SerializedName("refund_status")
    @Expose
    private String refundStatus;

    @SerializedName("refund_datetime")
    @Expose
    private String refundDatetime;

    @SerializedName("approve_return_amt")
    @Expose
    private String approveReturnAmt;

    @SerializedName("approved_status")
    @Expose
    private String approvedStatus;

    @SerializedName("approved_date")
    @Expose
    private String approvedDate;

    public String getCustomerReason() {
        return customerReason;
    }

    public void setCustomerReason(String customerReason) {
        this.customerReason = customerReason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApprovedDescription() {
        return approvedDescription;
    }

    public void setApprovedDescription(String approvedDescription) {
        this.approvedDescription = approvedDescription;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRefundDatetime() {
        return refundDatetime;
    }

    public void setRefundDatetime(String refundDatetime) {
        this.refundDatetime = refundDatetime;
    }

    public String getApproveReturnAmt() {
        return approveReturnAmt;
    }

    public void setApproveReturnAmt(String approveReturnAmt) {
        this.approveReturnAmt = approveReturnAmt;
    }

    public String getApprovedStatus() {
        return approvedStatus;
    }

    public void setApprovedStatus(String approvedStatus) {
        this.approvedStatus = approvedStatus;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }
}
