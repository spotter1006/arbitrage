# Wholesale Electricity Markets Analysis
## Postgres database popultated with EIA LMP data by: https://github.com/spotter1006/eac
## API:
GET /api/analysis - lists all analysis specs
POST /api/analysis - creates new analysis spec defined in the request body
PUT /api/analysis/{analysisId} - modifies and runs an existing anlysis spec. Report parameters in the request body. Outputs excel files
