package com.asmt.civiladvocacy.ui;

import com.asmt.civiladvocacy.data.entity.State;
import com.asmt.civiladvocacy.data.response.OfficialListResponse;

public class OfficialListState extends State.Success<OfficialListResponse> {
    public OfficialListState(OfficialListResponse data) {
        super(data);
    }
}
