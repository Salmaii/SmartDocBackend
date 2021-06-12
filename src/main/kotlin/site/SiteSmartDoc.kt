package site

import customer.Profile

class SiteSmartDoc {
    var currentProfile: Profile? = null
        private set

    private val profiles = mutableListOf<Profile>()

    fun createProfile(name: String, email: String) {
        val p = Profile(name = name, email = email)
        profiles.add(p)
    }

    fun login(email: String): Boolean {
        currentProfile = profiles.firstOrNull { p -> p.email == email }
        return currentProfile != null
    }
}