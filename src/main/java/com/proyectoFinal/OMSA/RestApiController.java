package com.proyectoFinal.OMSA; /**
 * Created by anyderre on 04/07/17.
 */
import com.proyectoFinal.OMSA.Entities.*;
import com.proyectoFinal.OMSA.Repository.*;
import com.google.gson.Gson;
import com.proyectoFinal.OMSA.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestApiController {
    @Autowired
    private AutobusServices autobusServices;
    @Autowired
    private ParadaServices paradaServices;
    @Autowired
    private ChequeoServices chequeoServices;
    @Autowired
    private CoordenadaServices coordenadaServices;
    @Autowired
    private RaspberryPiServices raspberryPiServices;
    @Autowired
    private RutaServices rutaServices;
    /*
     /**
     * Recuperando información desde la URL
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/autobus/buscar/{id}", method = RequestMethod.GET, produces = "application/json")
    public String autobus(@PathVariable Long id){
        Autobus autobus = autobusServices.buscarUnAutobus(id);
        if (autobus==null){
            return new Gson().toJson("no existe tal autobus");
        }
        Gson gson = new Gson();
        return gson.toJson(autobus);
    }
    /**
     *
     * Obtener Listado de autobus de una Ruta
     * @param id
     * @return
     */
    @RequestMapping(value ="/api/autobuses/buscar/rutas", method = RequestMethod.GET, produces = "application/json")
    public String obetenerAutobusPorRuta(@RequestParam("id_ruta")Long id){
        Ruta ruta =rutaServices.buscarRutaPorId(id);
        if(ruta==null){
            return new Gson().toJson("Esta ruta no existe");
        }
        List <Autobus> autobuses = autobusServices.buscarTodosLosAutobusporRuta(ruta.getId());
        return new Gson().toJson(autobuses);
    }

    /**
     *
     * guardar un autobus
     * @return
     */
    @RequestMapping(value ="/api/autobus/guardar", method = RequestMethod.POST, consumes = "application/json")
    public String guardarAutobus(@RequestBody Autobus autobus){
        Autobus autobus1 = autobusServices.guardarAutobus(autobus);
        if(autobus1!=null){
            return new Gson().toJson("Autobus guardado satisfactoriamentete");
        }
        return new Gson().toJson("no se ha podido guardar el autobus");
    }

    /**
     *
     * modificar coordenada de un autobus
     * @param numeroSerial
     * @param latitud
     * @param longitud
     * @param fechaRegistrada
     * @return
     */
    @RequestMapping(value = "/api/autobus/modificar/posicion/", method =RequestMethod.PUT, produces = "application/json", consumes = "application/json")
    public String modificarCoordenadaAutobus(@RequestParam("numeroSerial")String numeroSerial, @RequestParam("latitud") double latitud,
                                             @RequestParam("longitud")double longitud , @RequestParam("fecha") Long fechaRegistrada) {
        Autobus autobus = autobusServices.buscarAutobusPorRaspberryNumeroSerial(numeroSerial);

        if(autobus == null){
            return "El autobus que quieres modificar no existe";
        }
        autobus.setUltimaFechaModificada(fechaRegistrada);
        Coordenada coordenada = new Coordenada();
        coordenada.setLatitude(latitud);
        coordenada.setLongitud(longitud);
        autobus.setCoordenada(coordenada);

        if(autobusServices.modifcarCoordenadaAutobus(autobus)){
            return new Gson().toJson("Autobus modificado exitosamente");
        };
        return new Gson().toJson("no se pudo guardar el autobus");
    }
    /**
     *
     * modificar posicion de un autobus
     * @param estadoActual
     * @param fechaRegistrada
     * @param latitud
     * @param longitud
     * @return
     */
    @RequestMapping(value = "/api/autobus/modificar/estado", method =RequestMethod.PUT, produces = "application/json", consumes = "application/json")
    public String modificarEstadoAutobus(@RequestParam("estadoActual") Boolean estadoActual, @RequestParam("numeroSerial")String numeroSerial, @RequestParam("fecha") Long fechaRegistrada,
                                         @RequestParam("longitud")Double longitud, @RequestParam("latitud")Double latitud){
        Autobus autobus = autobusServices.buscarAutobusPorRaspberryNumeroSerial(numeroSerial);
        if(autobus == null){
            return new Gson().toJson("El autobus que quieres modificar no existe");
        }
        autobus.setActivo(estadoActual);
        autobus.setUltimaFechaModificada(fechaRegistrada);
        Coordenada coordenada = new Coordenada();
        coordenada.setLongitud(longitud);
        coordenada.setLatitude(latitud);
        //Parada parada = getParadaReal()
        if(autobusServices.modificarEstadoAutobus(autobus)){
            return new Gson().toJson( "Autobus modificado exitosamente");
        };
        return new Gson().toJson(
        "no se pudo guardar el autobus");

    }

    /**
     *
     * modificar posicion de un autobus
     * @param numeroSerial
     * @param cantidadPasajeros
     * @param  fechaRegistrada
     * @return
     */
    @RequestMapping(value = "/api/autobus/modificar/cantidadPasajeros", method =RequestMethod.PUT, produces = "application/json", consumes = "application/json")
    public String modificarCantiddadPasajerosAutobus( @RequestParam("numeroSerial") String numeroSerial,@RequestParam("cantidadPasajeros") Integer cantidadPasajeros, @RequestParam("fecha") Long fechaRegistrada){
        Autobus autobus = autobusServices.buscarAutobusPorRaspberryNumeroSerial(numeroSerial);
        if(autobus == null){
            return new Gson().toJson("El autobus que quieres modificar no existe");
        }
        autobus.setCantidadDePasajerosActual(cantidadPasajeros);
        autobus.setUltimaFechaModificada(fechaRegistrada);
        if(autobusServices.modificarCantidadPasajeros(autobus)){
            return new Gson().toJson("Autobus modificado exitosamente");
        };
        return new Gson().toJson("no se pudo guardar el autobus");

    }
//----------------------------------------Parada---------------------------------------
    @RequestMapping(value = "/api/paradas/ruta/{id}", method = RequestMethod.GET, produces = "application/json")
    public String buscarParadasPorRuta(@PathVariable Long id){
        Ruta ruta = rutaServices.buscarRutaPorId(id);
        if(ruta==null){
            return new Gson().toJson("esta ruta no existe");
        }
        List<Parada> paradas = paradaServices.buscarParadaPorRutaId(id);
        if(paradas.isEmpty()){
            return new Gson().toJson("no se encontro ninguna parada para esta ruta");
        }
        return new Gson().toJson(paradas);
    }
//---------------------------------------Ruta-------------------------------------------
    @RequestMapping(value = "/api/ruta/{id}", method = RequestMethod.GET, produces = "application/json")
    public String buscarRuta(@PathVariable Long id){
        Ruta ruta =rutaServices.buscarRutaPorId(id);
        if(ruta==null){
            return new Gson().toJson("Esta ruta no existe");
        }
        return  new Gson().toJson(ruta);
    }
//-------------------------------------Chequeo-----------------------------------------------

    @RequestMapping(value="/api/chequeo/guardar", method = RequestMethod.GET, consumes = "application/json")
    public String guardarChequeo(@RequestBody Chequeo chequeo){
        //obteniendo la parada mas cerca a ese punto
        chequeo.setParada(getParadaReal(chequeo));

        chequeo.setRaspberryPiAPI(raspberryPiServices.buscarRaspberryPiPorNumeroSerial(chequeo.getRaspberryPiAPI().getNumeroSerial()));
        return new Gson().toJson(chequeoServices.guardarChequeo(chequeo));
    }

    private Parada getParadaReal(Chequeo chequeo){
        String numeroSerial = chequeo.getRaspberryPiAPI().getNumeroSerial();
        Autobus autobus = autobusServices.buscarAutobusPorRaspberryNumeroSerial(numeroSerial);
        Ruta ruta = autobus.getRuta();
        List<Parada> paradas = paradaServices.buscarParadaPorRutaId(ruta.getId());
        Integer cont =1;
        double distancia=1000000000;
        double distanciaActual=0;
        Integer indexes=0;
        for(Parada parada:paradas){
            distanciaActual =Math.sqrt(Math.pow((parada.getCoordenada().getLatitude()-chequeo.getParada().getCoordenada().getLatitude()), 2)+Math.pow((parada.getCoordenada().getLongitud()-chequeo.getParada().getCoordenada().getLongitud()),2));
            if (distanciaActual<distancia){
                //distancia =distanciaActual;
                indexes = cont;
            }
            cont++;
        }

        return paradas.get(indexes);
    }

}
