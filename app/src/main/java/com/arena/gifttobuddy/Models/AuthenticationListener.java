package com.arena.gifttobuddy.Models;

public interface AuthenticationListener{
    public abstract void onSuccess(String accessToken);

    public abstract void onFail(String error);
}