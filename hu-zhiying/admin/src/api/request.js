import {
  arbitrationRows,
  contentRows,
  dashboard,
  dispatchRows,
  financeRows,
  marketingRows,
  masterRows,
  orderRows,
  pricingRows,
  systemRows,
} from '../mock/data';

const dataset = {
  dashboard,
  dispatchRows,
  orderRows,
  masterRows,
  pricingRows,
  financeRows,
  arbitrationRows,
  marketingRows,
  contentRows,
  systemRows,
};

export function getAdminMock(name) {
  return Promise.resolve(JSON.parse(JSON.stringify(dataset[name])));
}
