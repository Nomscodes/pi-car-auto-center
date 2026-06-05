package br.com.picarauto.controller;

import br.com.picarauto.controller.mapper.IPecaMapper;
import br.com.picarauto.model.PecaModel;
import br.com.picarauto.model.dto.PecaDTO;
import br.com.picarauto.service.IPecaService;

/**
 *
 * @author Caio4breu
 */
public class PecaController extends GenericController<PecaModel, PecaDTO, IPecaService, IPecaMapper> {

    public PecaController(IPecaService service, IPecaMapper mapper) {
        super(service, mapper);
    }

    public PecaDTO findByCodigoNacional(Integer codigoNacional) {
        PecaModel peca = service.findByCodigoNacional(codigoNacional);
        return mapper.toDto(peca);
    }
}