#version 330 core
out vec4 FragColor;

struct Material {
    sampler2D diffuse;
    sampler2D specular;
    float     shininess;
};

struct Light {
    vec3 position;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

in vec3 normal;
in vec3 fragPos;
in vec2 texCoords;

uniform Material material;
uniform Light light;
uniform vec3 viewPos;

void main() {
    //Ambient
    vec3 ambient = light.ambient * vec3(texture(material.diffuse, texCoords));

    vec3 norm = normalize(normal);
    vec3 lightDir = normalize(light.position - fragPos);

    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = light.diffuse * diff * vec3(texture(material.diffuse, texCoords));

    vec3 viewDir = normalize(viewPos - fragPos);
    vec3 reflectDir = reflect(-lightDir, norm);

    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 specular = vec3(texture(material.specular, texCoords)) * spec * light.specular;

    vec3 result = (ambient + diffuse + specular) * vec3(texture(material.diffuse, texCoords));

    FragColor = vec4(result, 1.0f);
}