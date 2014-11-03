package com.jeskeshouse.injectedtestrunner.components;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.jeskeshouse.injectedtestrunner.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.injectables.InjectableThing;

import javax.inject.Inject;
import javax.inject.Named;

import roboguice.content.RoboContentProvider;

public class TestProvider extends RoboContentProvider {

    @Inject
    public InjectableThing injectableThing;

    @Inject
    @Named("named")
    public AnotherInjectableThing namedThing;

    @Inject
    @Named("provided")
    public AnotherInjectableThing providedThing;

    @Inject
    @Named("providedByModule")
    public AnotherInjectableThing providedByModuleThing;

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
