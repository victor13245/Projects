#version 410 core

in vec3 fNormal;
in vec4 fPosEye;
in vec2 fTexCoords;
in vec4 fragPosLightSpace;

out vec4 fColor;

//lighting
uniform	vec3 lightDir;
uniform	vec3 lightColor;
uniform float fogDensity;

//texture
uniform sampler2D diffuseTexture;
uniform sampler2D specularTexture;
uniform sampler2D shadowMap;

vec3 ambient;
float ambientStrength = 0.2f;
vec3 diffuse;
vec3 specular;
float specularStrength = 0.5f;
float shininess = 32.0f;
float shadow;


//punctiform light
float constant = 1.0f;
float linear = 0.45f;
float quadratic = 0.44f;
float shininess2 = 160.0f;

//uniform vec3 lightPos;
in vec4 lightPosEye;
vec3 pointLightColor = vec3(1.0f,0.0f,0.0f); // culoare arbitrara

void computeLightComponents()
{		
	vec3 cameraPosEye = vec3(0.0f);//in eye coordinates, the viewer is situated at the origin
	
	//transform normal
	vec3 normalEye = normalize(fNormal);	
	
	//compute light direction
	vec3 lightDirN = normalize(lightDir);
	
	//compute view direction 
	vec3 viewDirN = normalize(cameraPosEye - fPosEye.xyz);
		
	//compute ambient light
	ambient = ambientStrength * lightColor;
	
	//compute diffuse light
	diffuse = max(dot(normalEye, lightDirN), 0.0f) * lightColor;
	
	//compute specular light
	vec3 reflection = reflect(-lightDirN, normalEye);
	float specCoeff = pow(max(dot(viewDirN, reflection), 0.0f), shininess);
	specular = specularStrength * specCoeff * lightColor;
}

float computeShadow()
{
	// perform perspective divide
	vec3 normalizedCoords = fragPosLightSpace.xyz / fragPosLightSpace.w;
	
	// Transform to [0,1] range
	normalizedCoords = normalizedCoords * 0.5 + 0.5;
	
	if (normalizedCoords.z > 1.0f)
		return 0.0f;
	
	// Get closest depth value from light's perspective
	float closestDepth = texture(shadowMap, normalizedCoords.xy).r;
	
	// Get depth of current fragment from light's perspective
	float currentDepth = normalizedCoords.z;
	
	// Check whether current frag pos is in shadow
	float bias = 0.001f;
	float shadow = currentDepth - bias > closestDepth ? 1.0f : 0.0f;
	
	return shadow;
}

float computeFog()
{
 //float fogDensity = 0.05f;
 float fragmentDistance = length(fPosEye);
 float fogFactor = exp(-pow(fragmentDistance * fogDensity, 2));

 return clamp(fogFactor, 0.0f, 1.0f);
}



void computePointLight()
{
	float dist = length(lightPosEye.xyz - vec3(fPosEye));
    float att = 1.0f / (constant + linear * dist + quadratic * (dist * dist));
	
	//pozitia camerei
	vec3 cameraPosEye = vec3(0.0f);
	
	vec3 normalEye = normalize(fNormal);
	
	vec3 lightDirN = normalize(lightPosEye.xyz - vec3(fPosEye.x,fPosEye.y,fPosEye.z));
	
	//compute view direction 
	vec3 viewDirN = normalize(cameraPosEye - fPosEye.xyz);
	
	
	//compute half vector
	vec3 halfVector = normalize(lightDirN + viewDirN);
	
	ambient += att * ambientStrength * pointLightColor;
	
	diffuse += att * max(dot(normalEye, lightDirN), 0.0f) * pointLightColor;
	
	vec3 reflection = reflect(-lightDirN, normalEye);
	float specCoeff = pow(max(dot(normalEye, halfVector), 0.0f), shininess2);
	specular += att * specularStrength * specCoeff * pointLightColor;	
}



void main() 
{
	computeLightComponents();
	computePointLight();
	
	vec3 baseColor = vec3(0.9f, 0.35f, 0.0f);//orange
	
	ambient *= texture(diffuseTexture, fTexCoords).rgb;
	diffuse *= texture(diffuseTexture, fTexCoords).rgb;
	specular *= texture(specularTexture, fTexCoords).rgb;

	//modulate with shadow
	shadow = computeShadow();
	vec3 color = min((ambient + (1.0f - shadow)*diffuse) + (1.0f - shadow)*specular, 1.0f);
    
    float fogFactor = computeFog();
	vec4 fogColor = vec4(0.5f, 0.5f, 0.5f, 1.0f);
	fColor = mix(fogColor, vec4(color, 1.0f), fogFactor);
}
