package cl.usm.gestionPeliculasMemoria.repositories;

import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PeliculasRepositoryImplTest {

    PeliculasRepositoryImpl peliculasRepository;

    @BeforeEach
    void setUp() {
        peliculasRepository = new PeliculasRepositoryImpl();
    }
    @Test
    void insert_ok() {
        Pelicula pelicula = new Pelicula("1", "Minion", "Nolan", null, null);
        Pelicula resultado = peliculasRepository.insert(pelicula);

        assertNotNull(resultado);
        assertEquals("1", resultado.getId());
        assertEquals("Minion", resultado.getTitulo());
    }

    @Test
    void insert_idNulo_lanzaExcepcion() {
        Pelicula pelicula = new Pelicula(null, "Minion", "Nolan", null, null);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            peliculasRepository.insert(pelicula);
        });
        assertEquals("El ID de la pelicula no puede ser nulo", ex.getMessage());
    }

    @Test
    void insert_idDuplicado_lanzaExcepcion() {
        Pelicula pelicula1 = new Pelicula("1", "Calama", "patricio", null, null);
        Pelicula pelicula2 = new Pelicula("1", "test", "pereira", null, null);

        peliculasRepository.insert(pelicula1);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            peliculasRepository.insert(pelicula2);
        });
        assertEquals("La pelicula con ID 1 ya existe", ex.getMessage());
    }
    @Test
    void findById_existente_retornaPelicula() {
        Pelicula pelicula = new Pelicula("abc", "Matrix", "pepe", null, null);
        peliculasRepository.insert(pelicula);

        Pelicula resultado = peliculasRepository.findById("abc");

        assertNotNull(resultado);
        assertEquals("Matrix", resultado.getTitulo());
    }

    @Test
    void findById_noExistente_retornaNull() {
        Pelicula resultado = peliculasRepository.findById("no-existe");

        assertNull(resultado);
    }

    @Test
    void findById_idNulo_retornaNull() {
        Pelicula resultado = peliculasRepository.findById(null);

        assertNull(resultado);
    }

    @Test
    void findById_caseInsensitive() {
        Pelicula pelicula = new Pelicula("ABC", "Matrix", "pepe", null, null);
        peliculasRepository.insert(pelicula);

        Pelicula resultado = peliculasRepository.findById("abc");

        assertNotNull(resultado);
        assertEquals("Matrix", resultado.getTitulo());
    }

    @Test
    void findAll_vacio_retornaListaVacia() {
        List<Pelicula> resultado = peliculasRepository.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void findAll_conDatos_retornaTodasLasPeliculas() {
        peliculasRepository.insert(new Pelicula("1", "Minion", "Nolan", null, null));
        peliculasRepository.insert(new Pelicula("2", "calama", "pereira", null, null));

        List<Pelicula> resultado = peliculasRepository.findAll();

        assertEquals(2, resultado.size());
    }
}
