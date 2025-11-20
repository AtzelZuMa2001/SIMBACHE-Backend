package com.korealm.simbache.services.interfaces;

import java.util.Map;

public interface ColoniasService {
    Map<Long, String> getColoniasByLocality(String token, int localityId);
}
