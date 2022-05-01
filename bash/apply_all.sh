kubectl apply $(ls pgadmin*.yaml | awk ' { print " -f " $1 } ')
kubectl apply $(ls rabbitmq*.yaml | awk ' { print " -f " $1 } ')
kubectl apply $(ls dbpostgresql*.yaml | awk ' { print " -f " $1 } ')
sleep 25
kubectl apply $(ls app*.yaml | awk ' { print " -f " $1 } ')
kubectl apply $(ls frontend*.yaml | awk ' { print " -f " $1 } ')