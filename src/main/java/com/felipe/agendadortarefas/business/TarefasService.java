package com.felipe.agendadortarefas.business;

import com.felipe.agendadortarefas.business.dto.TarefasDTO;
import com.felipe.agendadortarefas.business.mapper.TarefasConverter;
import com.felipe.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.felipe.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.felipe.agendadortarefas.infrastructure.repository.TarefasRepository;
import com.felipe.agendadortarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefasConverter tarefaConverter;
    private final JwtUtil jwtUtil;


    public TarefasDTO gravarTarefa(String token, TarefasDTO dto) {
        String email = jwtUtil.extrarEmailToken(token.substring(7));
        dto.setDataCriacao(LocalDateTime.now());
        dto.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        dto.setEmailUsuario(email);
        TarefasEntity entity = tarefaConverter.paraTarefaEntity(dto);

        return tarefaConverter.paraTarefaDTO(
                tarefasRepository.save(entity));
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return tarefaConverter.paraListaTarefasDTO(
                tarefasRepository.findByDataEventoBetween(dataInicial, dataFinal));
    }

    public List<TarefasDTO> buscaTarefasPorEmail(String token) {
        String email = jwtUtil.extrarEmailToken(token.substring(7));
        List<TarefasEntity> listaTarefas = tarefasRepository.findByEmailUsuario(email);
        return tarefaConverter.paraListaTarefasDTO(listaTarefas);
    }
}
