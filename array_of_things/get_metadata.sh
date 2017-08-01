curl -o network.json http://plenar.io/v1/api/sensor-networks/array_of_things_chicago/
curl -o nodes.json http://plenar.io/v1/api/sensor-networks/array_of_things_chicago/nodes
curl -o sensors.json http://plenar.io/v1/api/sensor-networks/array_of_things_chicago/sensors
curl -o features.json http://plenar.io/v1/api/sensor-networks/array_of_things_chicago/features/
mkdir -p nodes
mkdir -p sensors
mkdir -p features
nodesArr=( "0000001e0610b9e7" "0000001e0610b9fd" "0000001e0610ba72" "0000001e0610ba89")
for i in "${nodesArr[@]}"
do
	curl -o nodes/$i.json http://plenar.io/v1/api/sensor-networks/array_of_things_chicago/nodes/$i
done
sensorsArr=("apds-9006-020" "bmi160" "bmp180" "chemsense" "hih4030" "hih6130" "hmc5883l" "htu21d" "lps25h" "ml8511" "mlx75305" "mma8452q" "pr103j2" "sht25" "tmp112" "tmp421" "tsl260rd" "tsys01")
for i in "${sensorsArr[@]}"
do
	curl -o sensors/$i.json http://plenar.io/v1/api/sensor-networks/array_of_things_chicago/sensors/$i
done
featuresArr=("acceleration" "atmospheric_pressure" "gas_concentration" "light_intensity" "magnetic_field" "orientation" "relative_humidity" "temperature")
for i in "${featuresArr[@]}"
do
	curl -o features/$i.json http://plenar.io/v1/api/sensor-networks/array_of_things_chicago/features/$i
done