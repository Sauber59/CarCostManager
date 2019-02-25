package com.example.damo.carcostmanager.interfaces;

/**Interfejs implementujący metody niezbędne do usuwania dodanych kosztów z bazy danych*/

public interface interface_delete {

    public void showUpdateDialog(final String costId);

    public void deleteCost(String costId);
}
