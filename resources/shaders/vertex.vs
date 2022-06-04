#version 330 core

layout(location = 0) in vec3 i_position;
layout(location = 1) in vec3 i_color;

out vec3 o_color;

uniform mat4 u_projMat;
uniform mat4 u_viewMat;

void main() {
    gl_Position =u_projMat * u_viewMat * vec4(i_position, 1);
    o_color = i_color;
    
}