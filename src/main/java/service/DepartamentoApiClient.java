package service;

import com.google.gson.Gson;
import model.Departamento;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DepartamentoApiClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private final String URL_API = "http://localhost:8080/api/departamentos";


    public Departamento[] obtenerDepartamentos() throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_API))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Entro en 200");
            return gson.fromJson(response.body(), Departamento[].class);
        } else {
            throw new RuntimeException("Respuesta inesperado del servidor. Codigo HTTP"+response.statusCode());
        }
    }

    public boolean insertarDepartamento(Departamento departamento) throws IOException, InterruptedException {
        String json = gson.toJson(departamento);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_API))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return true;
        } else {
            throw new RuntimeException("Respuesta inesperado del servidor. Codigo HTTP"+response.statusCode());
        }
    }

    public boolean eliminarDepartamento(String id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_API+"/"+id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 204) {
            return true;
        } else {
            throw new RuntimeException("Respuesta inesperado del servidor. Codigo HTTP "+response.statusCode());
        }
    }

    public boolean actualizarDepartamento(String id, Departamento departamento) throws IOException, InterruptedException {
        String json = gson.toJson(departamento);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_API+"/"+id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return true;
        } else {
            throw new RuntimeException("Respuesta inesperado del servidor. Codigo HTTP"+response.statusCode());
        }
    }
}
