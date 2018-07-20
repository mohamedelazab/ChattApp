package com.example.mohamed.chattapp.utils;

import android.support.annotation.NonNull;

import com.example.mohamed.chattapp.model.User;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Session {
    public static Session instance;
    private Realm realm;

    public static Session getInstance(){
        if (instance ==null){
            instance =new Session();
        }
        return instance;
    }
    public static Session newInstance(){
        return new Session();
    }

    private Session(){
        RealmConfiguration realmConfiguration =new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realm =Realm.getInstance(realmConfiguration);
    }

    public void loginUser(final User user){
        if (realm.where(User.class).findFirst() ==null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    realm.copyToRealm(user);
                }
            });
        }
        else {
            logoutUser();
            loginUser(user);
        }
    }

    public boolean isUserLoggedIn(){
        return realm.where(User.class).findFirst() !=null;
    }

    public void logoutUser() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.delete(User.class);
            }
        });
    }

    public User getUser(){
        return realm.where(User.class).findFirst();
    }
}
