/*
 * 				Twidere - Twitter client for Android
 * 
 *  Copyright (C) 2012-2014 Mariotaku Lee <mariotaku.lee@gmail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mariotaku.twidere.loader;

import android.content.Context;

import org.mariotaku.twidere.api.statusnet.model.Group;
import org.mariotaku.twidere.api.twitter.Twitter;
import org.mariotaku.twidere.api.twitter.TwitterException;
import org.mariotaku.twidere.api.twitter.model.ResponseList;
import org.mariotaku.twidere.model.ParcelableGroup;
import org.mariotaku.twidere.model.UserKey;

import java.util.List;

public class UserGroupsLoader extends BaseGroupsLoader {

    private final String mUserId;
    private final String mScreenName;

    public UserGroupsLoader(final Context context, final UserKey accountKey, final String userId,
                            final String screenName, final List<ParcelableGroup> data) {
        super(context, accountKey, 0, data);
        mUserId = userId;
        mScreenName = screenName;
    }

    @Override
    public ResponseList<Group> getGroups(final Twitter twitter) throws TwitterException {
        if (twitter == null) return null;
        if (mUserId != null)
            return twitter.getGroups(mUserId);
        else if (mScreenName != null) return twitter.getGroups(mScreenName);
        return null;
    }

    @Override
    protected boolean isMember(final Group list) {
        return true;
    }
}
