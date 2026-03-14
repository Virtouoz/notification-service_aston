package com.learn.notificationservice.assembler;

import com.learn.notificationservice.controller.NotificationController;
import com.learn.notificationservice.dto.response.NotificationResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class NotificationModelAssembler implements RepresentationModelAssembler<NotificationResponse, EntityModel<NotificationResponse>> {

    @Override
    public EntityModel<NotificationResponse> toModel(NotificationResponse response) {
        EntityModel<NotificationResponse> model = EntityModel.of(response);
        model.add(linkTo(methodOn(NotificationController.class).send(null)).withSelfRel());
        return model;
    }
}