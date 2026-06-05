package cl.usm.gestionPeliculasMemoria.controllers;

import cl.usm.gestionPeliculasMemoria.entities.Comentario;
import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import cl.usm.gestionPeliculasMemoria.services.PeliculasService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeliculasControllerTest {

    @InjectMocks
    PeliculasController peliculasController;

    @Mock
    PeliculasService peliculasService;


    @Test
    void getAll_sinQuery_retorna200ConLista() {
        List<Pelicula> peliculas = Arrays.asList(
                new Pelicula("1", "Minion", "Nolan", "token1", null),
                new Pelicula("2", "calama", "pereira", "token2", null)
        );

        when(peliculasService.getAll()).thenReturn(peliculas);

        ResponseEntity<List<Pelicula>> response = peliculasController.getAll(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(peliculasService, times(1)).getAll();
    }

    @Test
    void getAll_conQueryVacio_retorna200ConLista() {
        List<Pelicula> peliculas = Arrays.asList(
                new Pelicula("1", "Minion", "Nolan", "token1", null)
        );

        when(peliculasService.getAll()).thenReturn(peliculas);

        ResponseEntity<List<Pelicula>> response = peliculasController.getAll("");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAll_conQuery_retorna200ConFiltradas() {
        List<Pelicula> filtradas = Arrays.asList(
                new Pelicula("1", "Minion", "Nolan", "token1", null)
        );

        when(peliculasService.filter("mini")).thenReturn(filtradas);

        ResponseEntity<List<Pelicula>> response = peliculasController.getAll("mini");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Minion", response.getBody().get(0).getTitulo());
        verify(peliculasService, times(1)).filter("mini");
    }

    @Test
    void getAll_excepcion_retorna500() {
        when(peliculasService.getAll()).thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<List<Pelicula>> response = peliculasController.getAll(null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    void createPelicula_ok_retorna200() {
        Pelicula pelicula = new Pelicula("1", "prueba", "Nolan", null, null);
        Pelicula creada = new Pelicula("1", "prueba", "Nolan", "abc1234567", null);

        when(peliculasService.createPelicula(any(Pelicula.class))).thenReturn(creada);

        ResponseEntity<?> response = peliculasController.createPelicula(pelicula);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(peliculasService, times(1)).createPelicula(any(Pelicula.class));
    }

    @Test
    void createPelicula_servicioRetornaNull_retorna500() {
        Pelicula pelicula = new Pelicula("1", "prueba2", "Nolan", null, null);

        when(peliculasService.createPelicula(any(Pelicula.class))).thenReturn(null);

        ResponseEntity<?> response = peliculasController.createPelicula(pelicula);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    void findById_existente_retorna200() {
        Pelicula pelicula = new Pelicula("1", "mentitas_chocolate", "profe", "token1", null);

        when(peliculasService.findById("1")).thenReturn(pelicula);

        ResponseEntity<Pelicula> response = peliculasController.findById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mentitas_chocolate", response.getBody().getTitulo());
    }

    @Test
    void findById_noExistente_retorna404() {
        when(peliculasService.findById("999")).thenReturn(null);

        ResponseEntity<Pelicula> response = peliculasController.findById("999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getComentarios_peliculaConComentarios_retorna200() {
        Comentario[] comentarios = {
                new Comentario("user1", "Muy buena"),
                new Comentario("user2", "Excelente")
        };
        Pelicula pelicula = new Pelicula("1", "Matrix", "pepe", "token1", comentarios);

        when(peliculasService.findById("1")).thenReturn(pelicula);

        ResponseEntity<?> response = peliculasController.getComentarios("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getComentarios_peliculaNoExiste_retorna404() {
        when(peliculasService.findById("999")).thenReturn(null);

        ResponseEntity<?> response = peliculasController.getComentarios("999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
