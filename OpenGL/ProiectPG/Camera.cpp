#include "Camera.hpp"

namespace gps {

    //Camera constructor
    Camera::Camera(glm::vec3 cameraPosition, glm::vec3 cameraTarget, glm::vec3 cameraUp) {
        //TODO
        this->cameraPosition = cameraPosition;
        this->cameraTarget = cameraTarget;
        this->cameraUpDirection = cameraUp;

        this->cameraFrontDirection = glm::normalize(this->cameraTarget - this->cameraPosition);
        this->cameraRightDirection = glm::normalize(glm::cross(this->cameraFrontDirection, this->cameraUpDirection)); 
    }

    //return the view matrix, using the glm::lookAt() function
    glm::mat4 Camera::getViewMatrix() {
        //TODO

        return glm::lookAt(cameraPosition, cameraPosition + cameraFrontDirection, cameraUpDirection);
    }

    //update the camera internal parameters following a camera move event
    void Camera::move(MOVE_DIRECTION direction, float speed) {
        //TODO
        if (direction == gps::MOVE_FORWARD) {
            this->cameraPosition += speed * this->cameraFrontDirection;
        }

        if (direction == gps::MOVE_BACKWARD) {
            this->cameraPosition -= speed * this->cameraFrontDirection;
        }

        if (direction == gps::MOVE_LEFT) {
            this->cameraPosition -= speed * this->cameraRightDirection;
        }

        if (direction == gps::MOVE_RIGHT) {
            this->cameraPosition += speed * this->cameraRightDirection;
        }

        if (direction == gps::MOVE_UP) {
            this->cameraPosition += speed * this->cameraUpDirection;
        }

        if (direction == gps::MOVE_DOWN) {
            this->cameraPosition -= speed * this->cameraUpDirection;
        }

        this->cameraTarget = this->cameraPosition + this->cameraFrontDirection;
    }

    glm::vec3 Camera::getCameraPos()
    {
        return this->cameraPosition;
    }

    //update the camera internal parameters following a camera rotate event
    //yaw - camera rotation around the y axis
    //pitch - camera rotation around the x axis
    void Camera::rotate(float pitch, float yaw) {
        //TODO
        glm::vec3 directie;
        directie.x = cos(glm::radians(yaw)) * cos(glm::radians(pitch));
        directie.y = sin(glm::radians(pitch));
        directie.z = sin(glm::radians(yaw)) * cos(glm::radians(pitch));

        this->cameraFrontDirection = glm::normalize(directie);
        this->cameraTarget = this->cameraPosition + this->cameraFrontDirection;
        this->cameraRightDirection = glm::normalize(glm::cross(this->cameraFrontDirection, this->cameraUpDirection));
    }
}