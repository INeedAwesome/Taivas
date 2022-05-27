#version 330 core

in vec3 o_color;

out vec4 fo_color;

void main() {
    fo_color = vec4(o_color.x, o_color.y, o_color.z, 1);
}