package com.mygdx.game.dto.mapper;

import com.mygdx.game.controls.Controls;
import com.mygdx.game.controls.RemoteControls;
import com.mygdx.game.dto.ControlsDto;

public class ControlsMapper {
    public static void setRemoteControlsByDto(ControlsDto dto, RemoteControls controls) {
        controls.setForward(dto.getForward());
        controls.setLeft(dto.getLeft());
        controls.setRight(dto.getRight());
        controls.setBack(dto.getBack());
        controls.setShoot(dto.getShoot());
    }

    public static ControlsDto mapToDto(Controls controls) {
        return new ControlsDto(
                controls.forward(),
                controls.left(),
                controls.right(),
                controls.shoot(),
                controls.back()
        );
    }
}