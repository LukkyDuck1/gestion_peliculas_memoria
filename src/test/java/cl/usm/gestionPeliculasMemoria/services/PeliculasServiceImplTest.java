package cl.usm.gestionPeliculasMemoria.services;

import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import cl.usm.gestionPeliculasMemoria.repositories.PeliculasRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeliculasServiceImplTest {

    @InjectMocks
    PeliculasServiceImpl peliculasService;

    @Mock
    PeliculasRepository peliculasRepository;
    @Test
    void createPelicula_ok() {
        Pelicula pelicula = new Pelicula("1", "prueba", "Nolan", null, null);

        when(peliculasRepository.insert(any(Pelicula.class))).thenReturn(pelicula);

        Pelicula resultado = peliculasService.createPelicula(pelicula);

        assertNotNull(resultado);
        assertEquals("prueba", resultado.getTitulo());
        assertNotNull(resultado.getTokenDescarga());
        assertEquals(10, resultado.getTokenDescarga().length());
        verify(peliculasRepository, times(1)).insert(any(Pelicula.class));
    }

    @Test
    void createPelicula_repositoryLanzaExcepcion_retornaNull() {
        Pelicula pelicula = new Pelicula("1", "prueba2", "Nolan", null, null);

        when(peliculasRepository.insert(any(Pelicula.class)))
                .thenThrow(new IllegalArgumentException("La pelicula con ID 1 ya existe"));

        Pelicula resultado = peliculasService.createPelicula(pelicula);

        assertNull(resultado);
    }

    @Test
    void getAll_conDatos_retornaLista() {
        List<Pelicula> peliculas = Arrays.asList(
                new Pelicula("1", "minion", "Nolan", "token1", null),
                new Pelicula("2", "Matrix", "Wachowski", "token2", null)
        );

        when(peliculasRepository.findAll()).thenReturn(peliculas);

        List<Pelicula> resultado = peliculasService.getAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(peliculasRepository, times(1)).findAll();
    }

    @Test
    void getAll_sinDatos_retornaListaVacia() {
        when(peliculasRepository.findAll()).thenReturn(Collections.emptyList());

        List<Pelicula> resultado = peliculasService.getAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
    @Test
    void findById_existente_retornaPelicula() {
        Pelicula pelicula = new Pelicula("1", "mentitas_chocolate", "profe", "token1", null);

        when(peliculasRepository.findById("1")).thenReturn(pelicula);

        Pelicula resultado = peliculasService.findById("1");

        assertNotNull(resultado);
        assertEquals("mentitas_chocolate", resultado.getTitulo());
        verify(peliculasRepository, times(1)).findById("1");
    }

    @Test
    void findById_noExistente_retornaNull() {
        when(peliculasRepository.findById("999")).thenReturn(null);

        Pelicula resultado = peliculasService.findById("999");

        assertNull(resultado);
    }

}
