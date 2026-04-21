package com.server.database.productores;

import com.server.database.fabricas.*;


public class ProductorFabricaBaseDatos {

    public static FabricaBaseDatos obtenerFabrica(String tipoBaseDatos) {
        if (tipoBaseDatos.equalsIgnoreCase("mysql")) {
            return new FabricaBaseDatosMySQL();
        } else if (tipoBaseDatos.equalsIgnoreCase("h2")) {
            return new FabricaBaseDatosH2();
        }
        throw new IllegalArgumentException("Tipo de base de datos no soportado: " + tipoBaseDatos);
    }
}
