package com.asmt.civiladvocacy.data.response;

import com.asmt.civiladvocacy.data.entity.Address;
import com.asmt.civiladvocacy.data.entity.Office;

import java.util.List;

public class OfficialListResponse {
    public final Address address;
    public final List<Office> offices;

    public OfficialListResponse(Address address, List<Office> offices) {
        this.address = address;
        this.offices = offices;
    }
}
