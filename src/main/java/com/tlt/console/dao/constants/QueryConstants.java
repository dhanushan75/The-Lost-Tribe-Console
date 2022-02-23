package com.tlt.console.dao.constants;

public class QueryConstants {

    public static final String GET_CLIENT_ID_LIST_FOR_MONTH = " select DISTINCT ccc.client_id from "
            + " client_check_in_calendar ccc "
            + " where ccc.booked_date >= :pCurrentDate order by ccc.booked_date desc ";

    public static final String GET_CHECKIN_CHECKOUT_DATE_FOR_CLIENT_IDS =  " select  "
            + " unit_id, ccc.client_id, min(ccc.booked_date) checkin, max(ccc.booked_date) checkout "
            + " FROM tlt_console.client_check_in_calendar ccc "
            + " where ccc.client_id in (:pClientIdSet) group by client_id";

    public static final String GET_CASHIN_CASHOUT_FOR_CLIENT_IDS =  " select  "
            + " client_id , sum(cash_in) cash_in , sum(cash_out) cash_out "
            + " from tlt_console.client_expense "
            + " where client_id in (:pClientIdSet) ";

    public static final String GET_UNIT_AND_ROOM_FOR_UNIT_ID =  " select  "
            + " u.name room, uu.name unit "
            + " from tlt_console.units u , tlt_console.units uu "
            + " where u.unit_id = (:pUnitId) and uu.unit_id = u.parent_unit_id;";

    public static final String GET_DISTINCT_ID_PROOF_TYPE = " select  "
            + " distinct(id_proof_type) id_proof_type"
            + " from tlt_console.client_detail;";

    public static final String GET_CLIENT_ID_LIST = " select  "
            + " client_id"
            + " from tlt_console.client_detail;";

}
