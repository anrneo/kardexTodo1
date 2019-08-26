package com.example.kardex.ui.login;

public class Product {

    Number prodId;
    String producto;
    String cantidad;
    String usuario;

    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public Product(Number prodId, String producto, String cantidad, String usuario) {
        this.prodId = prodId;
        this.producto = producto;
        this.cantidad = cantidad;
        this.usuario = usuario;
    }

    public Number getProdId() {
        return prodId;
    }

    public String getProducto() {
        return producto;
    }

    public String getCantidad() {
        return cantidad;
    }

    public String getUsuario() {
        return usuario;
    }
}


