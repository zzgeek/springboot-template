package zzgeek.conf;

import zzgeek.commons.SiteUserContext;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public final class GlobalCache {


    public static final Map<String, SiteUserContext> USER_CONTEXT_MAP = new ConcurrentHashMap<>();

    //redis  缓存刷新token
    public static void doUserLogin(SiteUserContext suc) {
        assert suc != null;
        synchronized (USER_CONTEXT_MAP) {
            for (Map.Entry<String, SiteUserContext> entry : USER_CONTEXT_MAP.entrySet()) {
                if (suc.getAdminAccount().equals(entry.getValue().getAdminAccount())) {
                    USER_CONTEXT_MAP.remove(entry.getKey());
                }
              }
            USER_CONTEXT_MAP.put(suc.getToken(), suc);
        }
    }

    // redis 获取token
    public static SiteUserContext getCurrentContext(String token) {
        return USER_CONTEXT_MAP.get(token);
    }

}
