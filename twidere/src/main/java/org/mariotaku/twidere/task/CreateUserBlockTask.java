package org.mariotaku.twidere.task;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.mariotaku.sqliteqb.library.Expression;
import org.mariotaku.twidere.R;
import org.mariotaku.twidere.api.twitter.Twitter;
import org.mariotaku.twidere.api.twitter.TwitterException;
import org.mariotaku.twidere.api.twitter.model.User;
import org.mariotaku.twidere.model.ParcelableAccount;
import org.mariotaku.twidere.model.ParcelableCredentials;
import org.mariotaku.twidere.model.ParcelableUser;
import org.mariotaku.twidere.model.message.FriendshipTaskEvent;
import org.mariotaku.twidere.model.util.ParcelableAccountUtils;
import org.mariotaku.twidere.provider.TwidereDataStore;
import org.mariotaku.twidere.provider.TwidereDataStore.CachedRelationships;
import org.mariotaku.twidere.util.Utils;

/**
 * Created by mariotaku on 16/3/11.
 */
public class CreateUserBlockTask extends AbsFriendshipOperationTask {
    public CreateUserBlockTask(Context context) {
        super(context, FriendshipTaskEvent.Action.BLOCK);
    }

    @NonNull
    @Override
    protected User perform(@NonNull Twitter twitter, @NonNull ParcelableCredentials credentials,
                           @NonNull Arguments args) throws TwitterException {
        switch (ParcelableAccountUtils.getAccountType(credentials)) {
            case ParcelableAccount.Type.FANFOU: {
                return twitter.createFanfouBlock(args.userKey.getId());
            }
        }
        return twitter.createBlock(args.userKey.getId());
    }

    @Override
    protected void succeededWorker(@NonNull Twitter twitter,
                                   @NonNull ParcelableCredentials credentials,
                                   @NonNull Arguments args, @NonNull ParcelableUser user) {
        final ContentResolver resolver = context.getContentResolver();
        Utils.setLastSeen(context, args.userKey, -1);
        for (final Uri uri : TwidereDataStore.STATUSES_URIS) {
            final Expression where = Expression.and(
                    Expression.equalsArgs(TwidereDataStore.AccountSupportColumns.ACCOUNT_KEY),
                    Expression.equalsArgs(TwidereDataStore.Statuses.USER_KEY)
            );
            final String[] whereArgs = {args.accountKey.toString(), args.userKey.toString()};
            resolver.delete(uri, where.getSQL(), whereArgs);

        }
        // I bet you don't want to see this user in your auto complete list.
        final ContentValues values = new ContentValues();
        values.put(CachedRelationships.ACCOUNT_KEY, args.accountKey.toString());
        values.put(CachedRelationships.USER_KEY, args.userKey.toString());
        values.put(CachedRelationships.BLOCKING, true);
        values.put(CachedRelationships.FOLLOWING, false);
        values.put(CachedRelationships.FOLLOWED_BY, false);
        resolver.insert(CachedRelationships.CONTENT_URI, values);
    }

    @Override
    protected void showSucceededMessage(@NonNull Arguments params, @NonNull ParcelableUser user) {
        final boolean nameFirst = preferences.getBoolean(KEY_NAME_FIRST);
        final String message = context.getString(R.string.blocked_user, manager.getDisplayName(user,
                nameFirst));
        Utils.showInfoMessage(context, message, false);

    }

    @Override
    protected void showErrorMessage(@NonNull Arguments params, @Nullable Exception exception) {
        Utils.showErrorMessage(context, R.string.action_blocking, exception, true);
    }
}
