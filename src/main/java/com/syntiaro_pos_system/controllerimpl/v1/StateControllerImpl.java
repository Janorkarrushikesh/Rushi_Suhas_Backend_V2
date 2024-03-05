package com.syntiaro_pos_system.controllerimpl.v1;

import com.syntiaro_pos_system.controller.v1.StateController;
import com.syntiaro_pos_system.entity.v1.State;
import com.syntiaro_pos_system.repository.v1.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class StateControllerImpl implements StateController {
    private final StateRepository stateRepository;

    @Autowired
    public StateControllerImpl(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public List<State> getAllStates() {
        return stateRepository.findAll();
    }

    @Override
    public ResponseEntity<State> getStateById(@PathVariable Long id) {
        Optional<State> state = stateRepository.findById(id);
        return state.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<State> createState(@RequestBody State state) {
        State createdState = stateRepository.save(state);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdState);
    }

}
