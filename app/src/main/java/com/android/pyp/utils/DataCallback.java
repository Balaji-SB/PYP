package com.android.pyp.utils;

import com.android.volley.VolleyError;

/**
 * Created by devel-73 on 26/7/17.
 */

public interface DataCallback {

    public void onSuccess(Object result);
    public void onError(VolleyError error);
}
