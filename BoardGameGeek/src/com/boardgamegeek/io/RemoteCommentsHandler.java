package com.boardgamegeek.io;

import static org.xmlpull.v1.XmlPullParser.END_DOCUMENT;
import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentResolver;
import android.util.Log;

import com.boardgamegeek.util.StringUtils;

public class RemoteCommentsHandler extends RemoteBggHandler {

	private static final String TAG = "RemoteCommentsHandler";
	private XmlPullParser mParser;
	private List<Comment> mComments = new ArrayList<Comment>();
	private int mCommentsCount;

	public List<Comment> getResults() {
		return mComments;
	}

	@Override
	public int getCount() {
		return mCommentsCount;
	}

	@Override
	protected void clearResults() {
		mComments.clear();
	}

	@Override
	protected String getRootNodeName() {
		return "items";
	}

	@Override
	public boolean parse(XmlPullParser parser, ContentResolver resolver, String authority)
			throws XmlPullParserException, IOException {

		mParser = parser;

		int type;
		while ((type = mParser.next()) != END_DOCUMENT) {
			if (type == START_TAG && Tags.ITEMS.equals(mParser.getName())) {
				parseItems();
			}
		}
		return false;
	}

	protected void parseItems() throws XmlPullParserException, IOException {
		final int depth = mParser.getDepth();
		int type;
		while (((type = mParser.next()) != END_TAG || mParser.getDepth() > depth) && type != END_DOCUMENT) {
			if (type == START_TAG && Tags.COMMENTS.equals(mParser.getName())) {

				mCommentsCount = StringUtils.parseInt(mParser.getAttributeValue(null, Tags.TOTAL_ITEMS));
				Log.i(TAG, "Expecting " + mCommentsCount + " comments");

				parseComments();
			}
		}
	}

	private void parseComments() throws XmlPullParserException, IOException {

		String tag = null;

		final int depth = mParser.getDepth();
		int type;
		while (((type = mParser.next()) != END_TAG || mParser.getDepth() > depth) && type != END_DOCUMENT) {
			if (type == START_TAG) {
				tag = mParser.getName();
				if (Tags.COMMENT.equals(tag)) {
					Comment comment = new Comment();
					comment.Username = mParser.getAttributeValue(null, Tags.USERNAME);
					comment.Rating = mParser.getAttributeValue(null, Tags.RATING);
					comment.Value = mParser.getAttributeValue(null, Tags.VALUE);
					mComments.add(comment);
				}
			}
		}
	}

	private interface Tags {
		// String ITEM = "item";
		// String ID = "id";
		String ITEMS = "items";
		String COMMENTS = "comments";
		// String PAGE = "page";
		String TOTAL_ITEMS = "totalitems";
		String COMMENT = "comment";
		String USERNAME = "username";
		String RATING = "rating";
		String VALUE = "value";
	}
}