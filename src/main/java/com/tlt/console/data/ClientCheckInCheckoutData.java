package com.tlt.console.data;

import java.util.Date;

public interface ClientCheckInCheckoutData {

    public Long getUnit_id();

    public Long getClient_id();

    public Date getCheckin();

    public Date getCheckout();

    public void setUnit_id(Long unit_id);

    public void setClient_id(Long client_id);

    public void setCheckin(Date checkin);

    public void setCheckout(Date checkout);

}
