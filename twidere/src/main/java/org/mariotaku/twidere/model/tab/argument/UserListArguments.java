package org.mariotaku.twidere.model.tab.argument;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by mariotaku on 16/3/6.
 */
@JsonObject
public class UserListArguments extends TabArguments {
    @JsonField(name = "list_id")
    long listId;

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    @Override
    public void copyToBundle(@NonNull Bundle bundle) {
        super.copyToBundle(bundle);
        bundle.putLong(EXTRA_LIST_ID, listId);
    }

    @Override
    public String toString() {
        return "UserListArguments{" +
                "listId=" + listId +
                "} " + super.toString();
    }
}
