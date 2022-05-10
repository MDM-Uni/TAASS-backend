kubectl port-forward service/frontend-web 4200:4200 &
kubectl port-forward service/app-utente 8080:8080 &
kubectl port-forward service/app-ospedale 8081:8081 &
kubectl port-forward service/app-negozio 8082:8082 &
kubectl port-forward service/api-gateway 8079:8079 &
kubectl port-forward service/pgadmin 5050:5050